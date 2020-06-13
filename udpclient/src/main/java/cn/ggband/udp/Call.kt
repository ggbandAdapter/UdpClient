package cn.ggband.udp

interface Call<T> {
    fun send(callback: UdpCallBack<T>)
}