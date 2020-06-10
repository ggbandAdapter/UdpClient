package cn.ggband.udp

import android.os.Handler
import android.os.Looper
import cn.ggband.udp.bean.CallbackParams
import cn.ggband.udp.interfaces.UdpResConvertInterface
import java.lang.reflect.Type
import java.net.InetAddress
import java.util.*
import kotlin.collections.ArrayList

class UdpCmdCallbackHelper constructor(
    private val convert: UdpResConvertInterface,
    private val timeOut: Long = 15000
) {

    private var isTimeOutLoop = false

    private val mCallbacks: MutableList<CallbackParams> by lazy {
        Collections.synchronizedList(
            ArrayList<CallbackParams>()
        )
    }

    fun add(taskId: String, callback: UdpCallBack<*>, returnType: Type?) {
        mCallbacks.add(CallbackParams(taskId, callback, returnType))
        if (!isTimeOutLoop) {
            startTimeOutLoop()
        }
    }

    private fun getCallback(taskId: String): CallbackParams? {

        val callBacks = mCallbacks.filter {
            if (taskId.isEmpty())
                true
            else
                it.taskId == taskId
        }.minBy { it.taskTime }
        mCallbacks.remove(callBacks)
        return callBacks

    }

    fun clear() {
        mCallbacks.clear()
    }

    private fun startTimeOutLoop() {
        isTimeOutLoop = true
        submit {
            while (isTimeOutLoop) {
                if (mCallbacks.isEmpty()) {
                    isTimeOutLoop = false
                } else {
                    dealTimeOutCallback()
                }
            }
        }

    }


    fun callback(data: ByteArray, address: InetAddress) {
        getCallback(convert.getTaskId(data))?.run {
            //回调到主线程
            Handler(Looper.getMainLooper()).post {
                callBack?.callback(
                    convert.getConvertContent(
                        data,
                        returnType
                    ), address
                )
            }

        }
    }


    private fun dealTimeOutCallback() {
        val timeOutCallbacks =
            mCallbacks.filter { System.currentTimeMillis() - it.taskTime > timeOut }
        timeOutCallbacks.forEach {
            it.callBack.timeOut()
        }
        mCallbacks.removeAll(timeOutCallbacks)
    }

    private fun stopTimeOutLoop() {
        isTimeOutLoop = false
    }
}