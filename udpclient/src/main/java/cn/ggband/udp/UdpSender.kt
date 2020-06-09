package cn.ggband.udp

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
                exception.printStackTrace()
                initFail()
            }
    }

    fun sendMessage(buf: ByteArray) {
        initSender()
        try {
            UdpThreadPoolManager.submit {
                mSendSKT?.send(buildSendPackage(buf))
            }
        } catch (exception: IOException) {
            exception.printStackTrace()
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