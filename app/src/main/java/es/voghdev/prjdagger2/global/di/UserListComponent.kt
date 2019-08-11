package es.voghdev.prjdagger2.global.di

import android.support.annotation.NonNull
import dagger.Component
import es.voghdev.prjdagger2.ui.activity.UserListActivity

@Component(dependencies = [RootComponent::class], modules = [UserListModule::class, MainModule::class])
interface UserListComponent {
    @NonNull
    fun inject(activity: UserListActivity)
}
