package cn.ggband.udp.interfaces

import java.lang.reflect.Type

/**
 * 接收信息转换接口
 */
interface UdpResConvertInterface {

    /**
     * 任务id --标识数据包（发送---接收）对应
     */
    fun getTaskId(buf: ByteArray): String

    /**
     * 转换 将ByteArray-->Bean
     */
    fun getConvertContent(data: ByteArray, returnType: Type?):Any

}