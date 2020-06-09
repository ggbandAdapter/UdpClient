package com.ipcom.iunifi.network.udp.ac.implments

import android.util.Log
import cn.ggband.udp.interfaces.UdpResConvertInterface
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * ac udp响应信息转换器
 */
class AcUdpResConvertImpl : UdpResConvertInterface {
    override fun getTaskId(buf: ByteArray): String {
        val dataStr = String(buf)
        return JSONObject(dataStr).optString("cmd")?:""
    }

    override fun getConvertContent(data: ByteArray, returnType: Type?): Any {
        if (returnType == null) return data
        val dataStr = String(data)
        return Gson().fromJson<Any>(dataStr, returnType)
    }
}