package cn.ggband.udp

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * UDP--线程管理
 */


private val simpleExecutor: ExecutorService by lazy {
    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
}

fun getExecutor(): ExecutorService {
    return simpleExecutor
}

/**
 * 子线程运行
 */
fun submit(body: () -> Unit) {
    simpleExecutor.execute {
        body()
    }
}

fun submitW() {
    simpleExecutor.execute {
        while (true) {
            Log.d("ggband", "run")
        }
    }
}

/**
 * 主线程运行
 */
fun runOnUi(body: () -> Unit) {
    Handler(Looper.getMainLooper()).post { body.invoke() }
}
