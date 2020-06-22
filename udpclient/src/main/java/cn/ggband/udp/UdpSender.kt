package cn.ggband.udp

import android.util.Log
import java.io.IOException
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

class UdpSender(private val ip: String, private val port: Int) {
    private var mSendSKT: MulticastSocket? = null

    fun sendMessage(buf: ByteArray) {
        try {
            mSendSKT?.send(buildSendPackage(buf))
            Log.d(UdpClient.LOG_TAG, "send udp package：" + String(buf))
        } catch (exception: IOException) {
            exception.printStackTrace()
            Log.d(UdpClient.LOG_TAG, "send udp package exception:$exception")
        }
    }

    init {
        if (mSendSKT == null || mSendSKT!!.isClosed)
            try {
                mSendSKT = MulticastSocket(port)
                mSendSKT!!.timeToLive = 6
                val address = InetAddress.getByName(ip)
                mSendSKT!!.joinGroup(address)
            } catch (exception: Exception) {
                Log.d(UdpClient.LOG_TAG, "init udp sender exception:$exception")
                exception.printStackTrace()
            }
    }


    private fun buildSendPackage(buf: ByteArray): DatagramPacket {
        return DatagramPacket(
            buf,
            buf.size,
            InetAddress.getByName(ip),
            port
        )
    }

}