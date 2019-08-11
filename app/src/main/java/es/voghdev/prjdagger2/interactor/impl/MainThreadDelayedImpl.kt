package es.voghdev.prjdagger2.interactor.impl

import android.os.Handler
import android.os.Looper
import es.voghdev.prjdagger2.interactor.MainThread

class MainThreadDelayedImpl(internal var delay: Long) : MainThread {
    private val handler: Handler = Handler(Looper.getMainLooper())
    
    override fun post(runnable: Runnable) {
        handler.postDelayed(runnable, delay)
    }
}
