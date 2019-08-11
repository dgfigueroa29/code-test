package es.voghdev.prjdagger2.global.di

import android.support.annotation.NonNull
import dagger.Component
import es.voghdev.prjdagger2.global.App
import es.voghdev.prjdagger2.ui.activity.BaseActivity
import es.voghdev.prjdagger2.ui.presenter.UserDetailPresenter
import es.voghdev.prjdagger2.ui.presenter.UserListPresenter

@Component(modules = [MainModule::class])
interface RootComponent {
    @NonNull
    fun inject(activity: BaseActivity)
    @NonNull
    fun inject(application: App)
    @NonNull
    fun inject(presenter: UserDetailPresenter)
    @NonNull
    fun inject(presenter: UserListPresenter)
}