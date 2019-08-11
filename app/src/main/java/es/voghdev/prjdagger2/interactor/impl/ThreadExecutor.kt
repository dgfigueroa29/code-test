package es.voghdev.prjdagger2.interactor.impl

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import es.voghdev.prjdagger2.interactor.Executor
import es.voghdev.prjdagger2.interactor.Interactor

class ThreadExecutor : Executor {
    private val threadPoolExecutor: ThreadPoolExecutor

    init {
        val corePoolSize = CORE_POOL_SIZE
        val maxPoolSize = MAX_POOL_SIZE
        val keepAliveTime = KEEP_ALIVE_TIME
        val timeUnit = TIME_UNIT
        val workQueue = WORK_QUEUE
        threadPoolExecutor = ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime.toLong(), timeUnit, workQueue as BlockingQueue<Runnable>?)
    }

    override fun run(interactor: Interactor) {
        threadPoolExecutor.submit { interactor.run() }
    }

    companion object {
        private const val CORE_POOL_SIZE = 3
        private const val MAX_POOL_SIZE = 5
        private const val KEEP_ALIVE_TIME = 120
        private val TIME_UNIT = TimeUnit.SECONDS
        private val WORK_QUEUE = LinkedBlockingQueue<Runnable>()
    }
}
