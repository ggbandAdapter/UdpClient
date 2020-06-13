package cn.ggband.udp

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * UDP--线程管理
 */


private val simpleExecutor: ExecutorService by lazy {
    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
}

/**
 * 子线程运行
 */
fun submit(body: () -> Unit): Future<*> {
    return simpleExecutor.submit(body)
}

/**
 * 主线程运行
 */
fun runOnUi(body: () -> Unit) {
    Handler(Looper.getMainLooper()).post { body.invoke() }
}
