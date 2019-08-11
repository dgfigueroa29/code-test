package es.voghdev.prjdagger2.ui.presenter

import es.voghdev.prjdagger2.global.model.User

class UserListContract {
    interface View: BaseContract.View {
        fun showUserList(users: List<User>)
        fun showUserListError(e: Exception)
        fun showNoInternetMessage()
        fun showLoading()
        fun hideLoading()
        fun makeUserSayHello(user: User)
        fun showUserClickedMessage(user: User)
    }

    interface Presenter: BaseContract.Presenter<View> {
    }
}