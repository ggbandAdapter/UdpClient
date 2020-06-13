package cn.ggband.udp

import android.util.Log
import java.io.IOException
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

class UdpSender(private val ip: String, private val port: Int) {
    private var mSendSKT: MulticastSocket? = null

    private fun initSender() {
        if (mSendSKT == null || mSendSKT!!.isClosed)
            try {
                mSendSKT = MulticastSocket(port)
                mSendSKT!!.timeToLive = 3
                val address = InetAddress.getByName(ip)
                mSendSKT!!.joinGroup(address)
            } catch (exception: Exception) {
                Log.d(UdpClient.LOG_TAG, "初始化udp发送器异常:$exception")
                exception.printStackTrace()
                initFail()
            }
    }

    fun sendMessage(buf: ByteArray) {
        initSender()
        try {
            submit {
                mSendSKT?.send(buildSendPackage(buf))
                Log.d(UdpClient.LOG_TAG, "已发送：" + String(buf))
            }

        } catch (exception: IOException) {
            exception.printStackTrace()
            Log.d(UdpClient.LOG_TAG, "发送udp信息异常:$exception")
            sendFail()
        }
    }


    private fun initFail() {

    }

    private fun sendFail() {}

    fun stop() {
        if (mSendSKT != null) {
            mSendSKT!!.close()
            mSendSKT = null
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