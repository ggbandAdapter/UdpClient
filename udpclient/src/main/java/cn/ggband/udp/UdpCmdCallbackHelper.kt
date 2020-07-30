package cn.ggband.udp

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

    private val descOutTime = if (timeOut > 0) timeOut else 4000

    private val mCallbacks: MutableList<CallbackParams> by lazy {
        Collections.synchronizedList(
            ArrayList<CallbackParams>()
        )
    }

    fun add(callback: UdpCallBack<*>, returnType: Type?) {
        Log.d(UdpClient.LOG_TAG, "add callback to stack=>;returnType:$returnType")
        mCallbacks.add(CallbackParams(callback, returnType))
        if (!isTimeOutLoop) {
            startTimeOutLoop()
        }
    }

    fun getCallback(callback: UdpCallBack<*>): CallbackParams? {

        return mCallbacks.filter {
            callback == it.callBack
        }.minBy { it.taskTime }
    }

    fun clear() {
        mCallbacks.clear()
    }

    private fun startTimeOutLoop() {
        submit {
            isTimeOutLoop = true
            while (isTimeOutLoop) {
                if (mCallbacks.isEmpty()) {
                    isTimeOutLoop = false
                } else {
                    dealTimeOutCallback()
                }
            }
        }
    }


    fun callback(callback: UdpCallBack<*>, data: ByteArray, address: InetAddress) {
        Log.d(UdpClient.LOG_TAG, "start callback.............")
        getCallback(callback)?.run {
            callBack?.onReceive(
                convert.getConvertContent(
                    data,
                    returnType
                ), address
            ) ?: kotlin.run {
                Log.d(UdpClient.LOG_TAG, "callback == null")
            }
        } ?: kotlin.run {
            Log.d(UdpClient.LOG_TAG, "callback not found........")
        }
    }


    private fun dealTimeOutCallback() {
        val timeOutCallbacks =
            mCallbacks.filter { System.currentTimeMillis() - it.taskTime > descOutTime }

        timeOutCallbacks.forEach {
            Log.d(UdpClient.LOG_TAG, "udp cmd:${it.returnType} receive finish!")
            it.callBack.onReceiveDone()
        }
        mCallbacks.removeAll(timeOutCallbacks)
    }


    fun isEmpty(): Boolean {
        return mCallbacks.isEmpty()
    }

    private fun stopTimeOutLoop() {
        isTimeOutLoop = false
    }
}