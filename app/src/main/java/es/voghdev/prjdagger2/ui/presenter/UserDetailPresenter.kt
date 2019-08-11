package es.voghdev.prjdagger2.ui.presenter

import android.content.Context
import javax.inject.Inject
import es.voghdev.prjdagger2.global.App
import es.voghdev.prjdagger2.global.di.RootComponent
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.interactor.ShowUserInteractor
import es.voghdev.prjdagger2.usecase.ShowUser

open class UserDetailPresenter @Inject
constructor(protected var context: Context, showUserInteractor: ShowUserInteractor) : UserDetailContract.Presenter {
    protected var interactor: ShowUser = showUserInteractor
    private lateinit var view: UserDetailContract.View
    
    private val component: RootComponent?
        get() = (context.applicationContext as App).component

    init {
        component!!.inject(this)
    }

    override fun initialize() {
        view.showLoading()
        interactor.getAsync(object : ShowUser.Listener {
            override fun onUserReceived(user: User, isCached: Boolean) {
                view.showUser(user)
                view.hideLoading()
            }

            override fun onError(e: Exception) {
                view.showUserError(e)
                view.hideLoading()
            }

            override fun onNoInternetAvailable() {
                view.showNoInternetMessage()
                view.hideLoading()
            }
        })
    }
    
    override fun attach(view: UserDetailContract.View) {
        this.view = view
    }

    override fun resume() {}

    override fun pause() {}

    override fun destroy() {}
}
