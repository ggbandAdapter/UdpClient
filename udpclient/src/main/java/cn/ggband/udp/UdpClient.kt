package cn.ggband.udp

import android.content.Context
import cn.ggband.udp.interfaces.UdpResConvertInterface
import java.lang.reflect.Proxy
import java.lang.reflect.Type

class UdpClient constructor(private val builder: Builder) {

    private val callbackHelper: UdpCmdCallbackHelper by lazy { UdpCmdCallbackHelper(builder.convert()) }
    private val receiver: UdpReceiver by lazy {
        UdpReceiver(
            builder.getIP(),
            builder.getRPort(),
            callbackHelper
        )
    }
    private val sender: UdpSender by lazy { UdpSender(builder.getIP(), builder.getsPort()) }

    init {
        MulticastLockManager.instance.acquire(builder.getContext())
    }

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service)
        ) { proxy, method, args ->
            val platform = Platform.get(this)
            platform.invokeDefaultMethod(method, service, proxy, args)
        } as T
    }


    fun send(taskId: String, buf: ByteArray, callBack: UdpCallBack<*>, returnType: Type? = null) {
        checkReceiverState()
        sender.sendMessage(buf)
        callbackHelper.add(taskId, callBack, returnType)
    }


    private fun checkReceiverState() {
        receiver.start()
    }

    fun release() {
        receiver.stop()
        sender.stop()
        callbackHelper.clear()
        MulticastLockManager.instance.release()
    }

    class Builder {

        private var ip: String = ""
        private var sPort: Int = 0
        private var rPort: Int = 0
        private var convert: UdpResConvertInterface? = null
        private var context: Context? = null

        fun getIP() = ip

        fun getsPort() = sPort

        fun getRPort() = rPort

        fun getContext() = context

        fun context(context: Context?): Builder {
            this.context = context
            return this
        }

        fun ip(ip: String): Builder {
            this.ip = ip
            return this
        }

        fun sPort(sPort: Int): Builder {
            this.sPort = sPort
            return this
        }

        fun rPort(rPort: Int): Builder {
            this.rPort = rPort
            return this
        }

        fun convert(convert: UdpResConvertInterface?): Builder {
            this.convert = convert
            return this
        }

        fun convert(): UdpResConvertInterface {
            return convert!!
        }

        fun build(): UdpClient {
            return UdpClient(this)
        }
    }


}