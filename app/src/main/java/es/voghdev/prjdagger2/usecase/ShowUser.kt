package es.voghdev.prjdagger2.usecase

import es.voghdev.prjdagger2.global.model.User

interface ShowUser {
    fun get(): User

    fun getAsync(listener: Listener?)

    interface Listener {
        fun onUserReceived(user: User, isCached: Boolean)
        fun onError(e: Exception)
        fun onNoInternetAvailable()
    }
}
