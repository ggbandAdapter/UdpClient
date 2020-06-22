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
            Log.d(UdpClient.LOG_TAG, "init udp receiver exception :$exception")
            exception.printStackTrace()
        }
    }


    fun receiveMessage() {
        Log.d(UdpClient.LOG_TAG, "========${this} looper receiving do thread ${Thread.currentThread().name}==========")
        try {
            val buff = ByteArray(bugLen)
            val datagramPacket = DatagramPacket(buff, buff.size)
            mReceiveSKT?.receive(datagramPacket)
            val data = datagramPacket.data
            datagramPacket.address
            val hostAddress = datagramPacket.address.hostAddress
            val dataStr = String(data, datagramPacket.offset, datagramPacket.length)
            Log.d(
                UdpClient.LOG_TAG,
                "received udp package from hostAddress: $hostAddress\ndata:$dataStr"
            )
            callbackHelper.callback(callBack, dataStr.toByteArray(), datagramPacket.address)
        } catch (exception: Exception) {
            Log.d(UdpClient.LOG_TAG, "received udp package exception:$exception")
            exception.printStackTrace()
        }
        Log.d(
            UdpClient.LOG_TAG,
            "received udp package isFinish:${callbackHelper.getCallback(callBack) == null}"
        )
        if (callbackHelper.getCallback(callBack) != null) {
            receiveMessage()
        }
    }


}