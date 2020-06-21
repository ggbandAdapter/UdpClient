package cn.ggband.udp

import android.content.Context
import cn.ggband.udp.interfaces.UdpResConvertInterface
import java.lang.reflect.Proxy
import java.lang.reflect.Type

class UdpClient constructor(private val builder: Builder) {
    companion object {
        val LOG_TAG = "udp_client"
    }

    private val callbackHelper: UdpCmdCallbackHelper by lazy {
        UdpCmdCallbackHelper(
            builder.convert(),
            builder.getOutTime()
        )
    }


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


    fun send(buf: ByteArray, callBack: UdpCallBack<*>, returnType: Type? = null) {
        submit {
            UdpReceiver(
                builder.getIP(),
                builder.getRPort(),
                callBack,
                callbackHelper
            ).receiveMessage()
            UdpSender(builder.getIP(), builder.getsPort()).sendMessage(buf)
            callbackHelper.add(callBack, returnType)
        }
    }


    fun release() {
        callbackHelper.clear()
        MulticastLockManager.instance.release()
    }

    class Builder {

        private var ip: String = ""
        private var sPort: Int = 0
        private var rPort: Int = 0
        private var convert: UdpResConvertInterface? = null
        private var context: Context? = null
        private var outTime: Long = 0

        fun getIP() = ip

        fun getsPort() = sPort

        fun getRPort() = rPort

        fun getContext() = context

        fun getOutTime(): Long = outTime

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

        fun outTime(outTime: Long): Builder {
            this.outTime = outTime
            return this
        }

        fun build(): UdpClient {
            return UdpClient(this)
        }
    }


}