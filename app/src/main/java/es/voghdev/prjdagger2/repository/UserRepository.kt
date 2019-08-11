package es.voghdev.prjdagger2.repository

import android.content.Context
import java.util.ArrayList
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.repository.cachepolicy.CachePolicy
import es.voghdev.prjdagger2.repository.cachepolicy.NoCachePolicy
import es.voghdev.prjdagger2.repository.cachepolicy.TimedCachePolicy
import es.voghdev.prjdagger2.usecase.GetUsers

class UserRepository(internal var context: Context, internal var apiDataSource: GetUsers, internal var fileDataSource: GetUsers) : GetUsers {
    internal var cachePolicy: CachePolicy
    internal var users: MutableList<User>? = ArrayList()

    init {
        this.cachePolicy = NoCachePolicy()
    }

    fun setCachePolicy(cachePolicy: CachePolicy) {
        this.cachePolicy = cachePolicy
    }

    override fun get(): List<User> {
        invalidateCacheIfNecessary(cachePolicy, users)

        if(users != null && users!!.size > 0) {
            return users as MutableList<User>
        }

        val apiUsers = apiDataSource.get() as MutableList<User>
        cachePolicy = TimedCachePolicy(TimedCachePolicy.ONE_MINUTE)
        users = apiUsers
        return users as List<User>
    }

    private fun invalidateCacheIfNecessary(cachePolicy: CachePolicy, users: MutableList<User>?) {
        if(!cachePolicy.isCacheValid) {
            users!!.clear()
        }
    }

    override fun getAsync(listener: GetUsers.Listener?) {
        invalidateCacheIfNecessary(cachePolicy, users)

        if (users != null && users!!.size > 0) {
            listener!!.onUsersReceived(users!!, true)
        } else {
            apiDataSource.getAsync(object : GetUsers.Listener {
                override fun onUsersReceived(users: List<User>, isCached: Boolean) {
                    listener!!.onUsersReceived(users, isCached)
                }

                override fun onError(e: Exception) {
                    listener!!.onError(e)
                }

                override fun onNoInternetAvailable() {
                    listener!!.onNoInternetAvailable()
                }
            })
        }
    }
}
