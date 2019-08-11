package es.voghdev.prjdagger2.global.di

import android.content.Context
import javax.inject.Named
import dagger.Module
import dagger.Provides
import es.voghdev.prjdagger2.datasource.api.GetUsersApiImpl
import es.voghdev.prjdagger2.datasource.api.ShowUserImpl
import es.voghdev.prjdagger2.datasource.datafile.GetUsersFileImpl
import es.voghdev.prjdagger2.global.App
import es.voghdev.prjdagger2.interactor.GetUsersInteractor
import es.voghdev.prjdagger2.interactor.ShowUserInteractor
import es.voghdev.prjdagger2.interactor.impl.MainThreadImpl
import es.voghdev.prjdagger2.interactor.impl.ThreadExecutor
import es.voghdev.prjdagger2.repository.UserRepository

@Module
class MainModule(private val application: App) {
    internal var showUserInteractor: ShowUserInteractor = ShowUserInteractor(ShowUserImpl(),
ThreadExecutor(),
MainThreadImpl())
    internal var interactor: GetUsersInteractor = GetUsersInteractor(GetUsersApiImpl(10, 0),
            ThreadExecutor(),
            MainThreadImpl())
    internal var userRepository: UserRepository

    init {
        userRepository = UserRepository(application, interactor, GetUsersFileImpl(application,
                ThreadExecutor(),
                MainThreadImpl()))
    }

    @Provides
    internal fun provideUserInteractor(): GetUsersInteractor {
        return interactor
    }

    @Provides
    internal fun provideShowUserInteractor(): ShowUserInteractor {
        return showUserInteractor
    }

    @Provides
    internal fun provideUserRepository(): UserRepository {
        return userRepository
    }

    @Provides
    @Named("applicationContext")
    internal fun provideApplicationContext(): Context {
        return application.applicationContext
    }
}