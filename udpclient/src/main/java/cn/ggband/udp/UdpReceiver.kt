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
            Log.d(UdpClient.LOG_TAG, "初始化udp接收器异常:$exception")
            initFail()
        }
    }

    fun start() {
        if (mReceiveSKT == null || mReceiveSKT!!.isClosed) {
            initReceiver()
        }
        submit {
            while (mReceiveSKT != null) {
                receiveMessage()
            }
        }
    }

    private fun receiveMessage() {
        try {
            Log.d(UdpClient.LOG_TAG, "========开始接收udp信息==========")
            val buff = ByteArray(bugLen)
            val datagramPacket = DatagramPacket(buff, buff.size)
            mReceiveSKT?.receive(datagramPacket)
            val data = datagramPacket.data
            datagramPacket.address
            val hostAddress = datagramPacket.address.hostAddress
            Log.d(UdpClient.LOG_TAG, "接收到 udp信息 来自 hostAddress: $hostAddress")
            val dataStr = String(data, datagramPacket.offset, datagramPacket.length)
            Log.d(UdpClient.LOG_TAG, "接收到 udp信息:$dataStr")
            callbackHelper.callback(dataStr.toByteArray(),datagramPacket.address)
        } catch (exception: Exception) {
            Log.d(UdpClient.LOG_TAG, "接收udp信息异常:$exception")
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