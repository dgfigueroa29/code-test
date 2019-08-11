package es.voghdev.prjdagger2.ui.presenter

import android.content.Context
import javax.inject.Inject
import es.voghdev.prjdagger2.global.App
import es.voghdev.prjdagger2.global.di.RootComponent
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.interactor.GetUsersInteractor
import es.voghdev.prjdagger2.usecase.GetUsers

open class UserListPresenter @Inject
constructor(protected var context: Context, getUsersInteractor: GetUsersInteractor?) : UserListContract.Presenter {
    protected var interactor: GetUsers = getUsersInteractor!!
    private lateinit var view: UserListContract.View

    private val component: RootComponent?
        get() = (context.applicationContext as App).component

    init {
        component!!.inject(this)
    }

    override fun initialize() {
        view.showLoading()
        interactor.getAsync(object : GetUsers.Listener {
            override fun onUsersReceived(users: List<User>, isCached: Boolean) {
                view.showUserList(users)
                view.hideLoading()
            }

            override fun onError(e: Exception) {
                view.showUserListError(e)
                view.hideLoading()
            }

            override fun onNoInternetAvailable() {
                view.showNoInternetMessage()
                view.hideLoading()
            }
        })
    }

    override fun resume() {}

    override fun pause() {}

    override fun destroy() {}
    
    override fun attach(view: UserListContract.View) {
        this.view = view
    }

    fun onUserPictureClicked(user: User) {
        view.makeUserSayHello(user)
    }

    fun onUserRowClicked(user: User) {
        view.showUserClickedMessage(user)
    }
}
