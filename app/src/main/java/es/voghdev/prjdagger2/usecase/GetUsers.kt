package es.voghdev.prjdagger2.usecase

import es.voghdev.prjdagger2.global.model.User

interface GetUsers {
    fun get(): List<User>

    fun getAsync(listener: Listener?)

    interface Listener {
        fun onUsersReceived(users: List<User>, isCached: Boolean)
        fun onError(e: Exception)
        fun onNoInternetAvailable()
    }
}
