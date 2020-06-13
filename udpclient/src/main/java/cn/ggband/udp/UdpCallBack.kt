package cn.ggband.udp


import java.net.InetAddress

interface UdpCallBack<T> {
    /**
     * Udp 接收回调
     * @param data upd 数据
     */
    fun onReceive(data: T,address: InetAddress)

    /**
     * 超时
     */
    fun onReceiveDone() {}

}