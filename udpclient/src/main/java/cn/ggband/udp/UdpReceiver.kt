package cn.ggband.udp
import android.util.Log
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

class UdpReceiver(
    private val ip: String,
    private val port: Int,
    private val callbackHelper: UdpCmdCallbackHelper
) {
    private var mReceiveSKT: MulticastSocket? = null
    private val bugLen = 1024


    private fun initReceiver() {
        try {
            mReceiveSKT = MulticastSocket(port)
            mReceiveSKT?.timeToLive = 1
            val address = InetAddress.getByName(ip)
            mReceiveSKT?.joinGroup(address)
        } catch (exception: Exception) {
            exception.printStackTrace()
            initFail()
        }
    }

    fun start() {
        if (mReceiveSKT == null || mReceiveSKT!!.isClosed) {
            initReceiver()
        }
        UdpThreadPoolManager.submit {
            while (mReceiveSKT != null) {
                receiveMessage()
            }
        }
    }

    private fun receiveMessage() {
        try {
            val buff = ByteArray(bugLen)
            val datagramPacket = DatagramPacket(buff, buff.size)
            mReceiveSKT?.receive(datagramPacket)
            val data = datagramPacket.data
            datagramPacket.address
            val hostAddress = datagramPacket.address.hostAddress
            val dataStr = String(data, datagramPacket.offset, datagramPacket.length)
            callbackHelper.callback(dataStr.toByteArray(),datagramPacket.address)
        } catch (exception: Exception) {
            exception.printStackTrace()
            receiveFail()
        }
    }


    private fun receiveFail() {}

    private fun initFail() {
    }

    /**
     * 停止接收数据
     */
    fun stop() {
        if (mReceiveSKT != null) {
            mReceiveSKT!!.close()
            mReceiveSKT = null
        }
    }

}