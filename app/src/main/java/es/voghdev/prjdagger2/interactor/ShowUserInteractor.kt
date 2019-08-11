package es.voghdev.prjdagger2.interactor

import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.usecase.ShowUser

class ShowUserInteractor(internal var showUser : ShowUser, internal var executor: Executor, internal var mainThread: MainThread) : Interactor, ShowUser, ShowUser.Listener {
    internal var listener : ShowUser.Listener = NullListener()

    override fun onUserReceived(user: User, isCached: Boolean) {
        listener.onUserReceived(user, isCached)
    }

    override fun onError(e: Exception) {
        listener.onError(e)
    }

    override fun onNoInternetAvailable() {
        listener.onNoInternetAvailable()
    }

    override fun run() {
        showUser.getAsync(listener)
    }

    override fun get(): User {
        throw IllegalArgumentException("Please use async version of this Interactor")
    }

    override fun getAsync(listener: ShowUser.Listener?) {
        if(listener != null) {
            this.listener = listener
        }
        this.executor.run(this)
    }

    private inner class NullListener : ShowUser.Listener {
        override fun onUserReceived(user : User, isCached: Boolean) {}
        override fun onError(e: Exception) {}
        override fun onNoInternetAvailable() {}
    }
}