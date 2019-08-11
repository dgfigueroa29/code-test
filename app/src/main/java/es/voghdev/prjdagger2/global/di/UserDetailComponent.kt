package es.voghdev.prjdagger2.global.di

import android.support.annotation.NonNull
import dagger.Component
import es.voghdev.prjdagger2.ui.activity.UserDetailActivity

@Component(dependencies = [RootComponent::class], modules = [UserDetailModule::class, MainModule::class])
interface UserDetailComponent {
    @NonNull
    fun inject(activity: UserDetailActivity)
}
