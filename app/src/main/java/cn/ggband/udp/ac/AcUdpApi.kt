package cn.ggband.udp.ac


import cn.ggband.udp.Call
import cn.ggband.udp.Constants
import cn.ggband.udp.UdpCallBack
import cn.ggband.udp.UdpClient
import cn.ggband.udp.ac.bean.ScanMessage
import cn.ggband.udp.ac.bean.ScanPackage
import cn.ggband.udp.anno.Field
import cn.ggband.udp.anno.Task
import com.ipcom.iunifi.network.udp.ac.implments.AcUdpResConvertImpl
import java.net.InetAddress


val acUdpClient =
    UdpClient.Builder().ip(Constants.DEVICE_AC_IP).sPort(Constants.DEVICE_AC_PORT)
        .rPort(Constants.RECEIVED_AC_PORT)
        .convert(AcUdpResConvertImpl()).build()

val acUdpApi = acUdpClient.create(AcUdpApi::class.java)


fun <T> Call<T>.send(suc: (T, InetAddress) -> Unit, fail: () -> Unit) {
    send(object : UdpCallBack<T> {
        override fun callback(data: T, address: InetAddress) {
            suc(data, address)
        }

        override fun timeOut() {
            fail()
        }
    })
}


interface AcUdpApi {

    /**
     * 扫描Ac
     */
    fun scan(
        @Field body: ScanPackage,
        @Task taskId: String = body.cmd.toString()
    ): Call<ScanMessage>

}