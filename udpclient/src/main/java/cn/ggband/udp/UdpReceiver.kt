package cn.ggband.udp

import android.util.Log
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

class UdpReceiver(
    ip: String,
    port: Int,
    private val callBack: UdpCallBack<*>,
    private val callbackHelper: UdpCmdCallbackHelper
) {
    private var mReceiveSKT: MulticastSocket? = null
    private val bugLen = 1024


    init {
        try {
            mReceiveSKT = MulticastSocket(port)
            mReceiveSKT?.timeToLive = 1
            val address = InetAddress.getByName(ip)
            mReceiveSKT?.joinGroup(address)
        } catch (exception: Exception) {
            Log.d(UdpClient.LOG_TAG, "初始化udp接收器异常:$exception")
            exception.printStackTrace()
        }
    }


    fun receiveMessage() {
        Log.d(UdpClient.LOG_TAG, "======== receiveMessage ==========")
        while (!callbackHelper.isEmpty()) {
            try {
                Log.d(UdpClient.LOG_TAG, "========循环接收udp信息==========")
                val buff = ByteArray(bugLen)
                val datagramPacket = DatagramPacket(buff, buff.size)
                mReceiveSKT?.receive(datagramPacket)
                val data = datagramPacket.data
                datagramPacket.address
                val hostAddress = datagramPacket.address.hostAddress
                Log.d(UdpClient.LOG_TAG, "接收到 udp信息 来自 hostAddress: $hostAddress")
                val dataStr = String(data, datagramPacket.offset, datagramPacket.length)
                Log.d(UdpClient.LOG_TAG, "接收到 udp信息:$dataStr")
                callbackHelper.callback(callBack, dataStr.toByteArray(), datagramPacket.address)
            } catch (exception: Exception) {
                Log.d(UdpClient.LOG_TAG, "接收udp信息异常:$exception")
                exception.printStackTrace()
            }
        }

    }


}