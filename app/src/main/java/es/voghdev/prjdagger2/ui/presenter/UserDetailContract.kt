package es.voghdev.prjdagger2.ui.presenter

import es.voghdev.prjdagger2.global.model.User

class UserDetailContract {
    interface View: BaseContract.View {
        fun showUser(user: User)
        fun showUserError(e: Exception)
        fun showNoInternetMessage()
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter: BaseContract.Presenter<View> {
    }
}