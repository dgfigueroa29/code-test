package es.voghdev.prjdagger2.global.di

import android.content.Context
import dagger.Module
import dagger.Provides
import es.voghdev.prjdagger2.ui.view.ShowUserClickedToastImpl
import es.voghdev.prjdagger2.ui.view.ShowUserGreetingToastImpl
import es.voghdev.prjdagger2.usecase.ShowUserClicked
import es.voghdev.prjdagger2.usecase.ShowUserGreeting

@Module
class UserListModule(internal var mContext: Context) {
    internal var showUserClickedToast: ShowUserClicked = ShowUserClickedToastImpl(mContext)
    internal var showUserGreetingToast: ShowUserGreeting = ShowUserGreetingToastImpl(mContext)

    @Provides
    fun provideShowUserClicked(): ShowUserClicked {
        return showUserClickedToast
    }

    @Provides
    fun provideShowUserGreeting(): ShowUserGreeting {
        return showUserGreetingToast
    }
}
