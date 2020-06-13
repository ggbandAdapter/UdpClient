package cn.ggband.udp

import android.os.Handler
import android.os.Looper
import android.util.Log
import cn.ggband.udp.bean.CallbackParams
import cn.ggband.udp.interfaces.UdpResConvertInterface
import java.lang.reflect.Type
import java.net.InetAddress
import java.util.*
import kotlin.collections.ArrayList

class UdpCmdCallbackHelper constructor(
    private val convert: UdpResConvertInterface,
    timeOut: Long = 0
) {

    private var isTimeOutLoop = false

    private val descOutTime = if (timeOut > 0) timeOut else 6000

    private val mCallbacks: MutableList<CallbackParams> by lazy {
        Collections.synchronizedList(
            ArrayList<CallbackParams>()
        )
    }

    fun add(taskId: String, callback: UdpCallBack<*>, returnType: Type?) {
        Log.d(UdpClient.LOG_TAG, "add callback to stack=>taskId:$taskId;returnType:$returnType")
        mCallbacks.add(CallbackParams(taskId, callback, returnType))
        if (!isTimeOutLoop) {
            startTimeOutLoop()
        }
    }

    private fun getCallback(taskId: String): CallbackParams? {

        return mCallbacks.filter {
            if (taskId.isEmpty())
                true
            else
                it.taskId == taskId
        }.minBy { it.taskTime }
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
        Log.d(UdpClient.LOG_TAG, "开始执行callback.............")
        getCallback(convert.getTaskId(data))?.run {
            //回调到主线程
            Handler(Looper.getMainLooper()).post {
                callBack?.onReceive(
                    convert.getConvertContent(
                        data,
                        returnType
                    ), address
                ) ?: kotlin.run {
                    Log.d(UdpClient.LOG_TAG, "callback == null")
                }
            }
        } ?: kotlin.run {
            Log.d(UdpClient.LOG_TAG, "callback.......CallbackParams == null")
        }
    }


    private fun dealTimeOutCallback() {
        val timeOutCallbacks =
            mCallbacks.filter { System.currentTimeMillis() - it.taskTime > descOutTime }
        timeOutCallbacks.forEach {
            Log.d(UdpClient.LOG_TAG, "udp cmd:${it.taskId} 响应超时")
            it.callBack.onReceiveDone()
        }
        mCallbacks.removeAll(timeOutCallbacks)
    }

    private fun stopTimeOutLoop() {
        isTimeOutLoop = false
    }
}