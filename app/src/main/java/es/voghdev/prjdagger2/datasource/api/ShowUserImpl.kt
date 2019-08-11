package es.voghdev.prjdagger2.datasource.api

import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.usecase.ShowUser

class ShowUserImpl : ShowUser{
    override fun get(): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAsync(listener: ShowUser.Listener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}