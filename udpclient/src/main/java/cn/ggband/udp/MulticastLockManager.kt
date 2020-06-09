package cn.ggband.udp

import android.content.Context
import android.net.wifi.WifiManager

/**
 * 组播管理
 */
class MulticastLockManager private constructor() {

    companion object {
        val instance: MulticastLockManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MulticastLockManager()
        }
    }

    private var multicastLock: WifiManager.MulticastLock? = null

    fun acquire(tag: String = "multicast.udp", context: Context) {
        if (multicastLock == null) {
            val wifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            multicastLock = wifiManager?.createMulticastLock(tag)
        }
        multicastLock?.acquire()
    }

    fun release() {
        multicastLock?.let {
            it.release()
            multicastLock = null
        }
    }

}