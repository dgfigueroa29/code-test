package es.voghdev.prjdagger2.interactor.impl

import android.os.Handler
import android.os.Looper
import es.voghdev.prjdagger2.interactor.MainThread

class MainThreadImpl : MainThread {
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun post(runnable: Runnable) {
        handler.post(runnable)
    }
}