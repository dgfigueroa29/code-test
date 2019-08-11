/*
 * Copyright (C) 2016 Olmo Gallegos Hernández
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.voghdev.prjdagger2

import android.content.Context

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

import es.voghdev.prjdagger2.global.App
import es.voghdev.prjdagger2.global.di.MainModule
import es.voghdev.prjdagger2.global.di.RootComponent
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.interactor.GetUsersInteractor
import es.voghdev.prjdagger2.ui.presenter.UserListPresenter
import es.voghdev.prjdagger2.usecase.GetUsers

import junit.framework.Assert.assertNotNull
import org.mockito.Matchers.any
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class UserListPresenterTest : BaseUnitTest() {

    @Mock
    internal var mockInteractor: GetUsersInteractor? = null
    @Mock
    internal var mockErrorInteractor: GetUsersInteractor? = null
    @Mock
    internal var mockApp: App? = null
    @Mock
    internal var mockComponent: RootComponent? = null
    @Mock
    internal var mockModule: MainModule? = null
    @Mock
    internal var mockContext: Context? = null
    @Mock
    internal var mockView: UserListView? = null
    @Mock
    internal var mockUser: User? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    @Throws(Exception::class)
    fun shouldCreateANonNullMockedPresenter() {
        val presenter = givenAMockedPresenter()
        assertNotNull(presenter)
    }

    @Test
    @Throws(Exception::class)
    fun shouldShowLoadingOnStart() {
        val presenter = givenAMockedPresenter()
        
        presenter.initialize()
        
        verify(mockView!!, times(1)).showLoading()
    }

    @Test
    @Throws(Exception::class)
    fun shouldCallGetUsersOnStart() {
        val presenter = givenAMockedPresenter()
        presenter.initialize()
        verify(mockInteractor!!, times(1)).getAsync(any(GetUsers.Listener::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun shouldShowNoInternetMessageWhenInternetIsNotAvailable() {
        val presenter = givenAMockedPresenterWithNoInternet()
        presenter.initialize()
        verify(mockView!!, times(1)).showNoInternetMessage()
        verify(mockView!!, times(1)).hideLoading()
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotShowUserListWhenInternetIsNotAvailable() {
        val presenter = givenAMockedPresenterWithNoInternet()
        presenter.initialize()
        verify(mockView!!, times(1)).hideLoading()
        verify(mockView!!, times(0)).showUserList(ArrayList())
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotShowAPIErrorWhenInternetIsNotAvailable() {
        val presenter = givenAMockedPresenterWithNoInternet()
        presenter.initialize()
        verify(mockView!!, times(1)).hideLoading()
        verify(mockView!!, times(0)).showUserListError(any(Exception::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun shouldShowErrorMessageWhenTheresAnErrorInTheAPI() {
        val presenter = givenAMockedPresenterWithBrokenAPI()
        presenter.initialize()
        verify(mockView!!, times(1)).hideLoading()
        verify(mockView!!, times(1)).showUserListError(any(Exception::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotShowNoInternetMessageWhenThereIsAnErrorInTheAPI() {
        val presenter = givenAMockedPresenterWithBrokenAPI()
        presenter.initialize()
        verify(mockView!!, times(1)).hideLoading()
        verify(mockView!!, times(0)).showNoInternetMessage()
    }

    @Test
    @Throws(Exception::class)
    fun shouldShowUserListAfterApiReturnsResults() {
        doAnswer(object : Answer<Any> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any? {
                val users = generateMockUserList()
                val callback = invocation.arguments[0] as GetUsers.Listener
                callback.onUsersReceived(users, true)
                return null
            }
        }).`when`(mockInteractor!!).getAsync(any(GetUsers.Listener::class.java))

        val presenter = givenAMockedPresenter()
        presenter.initialize()
        verify(mockView!!, times(1)).showUserList(ArrayList())
        verify(mockView!!, times(1)).hideLoading()
    }

    @Test
    @Throws(Exception::class)
    fun shouldDisplayUserNameWhenARowIsClicked() {
        val presenter = givenAMockedPresenter()
        mockUser?.let { presenter.onUserRowClicked(it) }
        mockUser?.let { verify(mockView!!, times(1)).showUserClickedMessage(it) }
        mockUser?.let { verify(mockView!!, times(0)).makeUserSayHello(it) }
    }

    @Test
    @Throws(Exception::class)
    fun shouldDisplayAHelloMessageWhenUserPictureIsClicked() {
        val presenter = givenAMockedPresenter()
        mockUser?.let { presenter.onUserPictureClicked(it) }
        mockUser?.let { verify(mockView!!, times(1)).makeUserSayHello(it) }
        mockUser?.let { verify(mockView!!, times(0)).showUserClickedMessage(it) }
    }

    @Test
    @Throws(Exception::class)
    fun shouldDoNothingOnResume() {
        val presenter = givenAMockedPresenter()
        presenter.resume()
        verify(mockInteractor!!, times(0)).getAsync(any(GetUsers.Listener::class.java))
        verify(mockErrorInteractor!!, times(0)).getAsync(any(GetUsers.Listener::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun shouldDoNothingOnPause() {
        val presenter = givenAMockedPresenter()
        presenter.pause()
        verify(mockInteractor!!, times(0)).getAsync(any(GetUsers.Listener::class.java))
        verify(mockErrorInteractor!!, times(0)).getAsync(any(GetUsers.Listener::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun shouldDoNothingOnDestroy() {
        val presenter = givenAMockedPresenter()
        presenter.destroy()
        verify(mockInteractor!!, times(0)).getAsync(any(GetUsers.Listener::class.java))
        verify(mockErrorInteractor!!, times(0)).getAsync(any(GetUsers.Listener::class.java))
    }

    private fun givenAMockedEnvironment() {
        `when`(mockContext!!.applicationContext).thenReturn(mockApp)
        `when`(mockApp!!.component).thenReturn(mockComponent)
        `when`(mockApp!!.mainModule).thenReturn(mockModule)
    }

    private fun givenAMockedPresenter(): UserListPresenter {
        givenAMockedEnvironment()
        val presenter = mockContext?.let { UserListPresenter(it, mockInteractor) }
        presenter!!.view = mockView!!
        return presenter
    }

    private fun givenAMockedPresenterWithNoInternet(): UserListPresenter {
        givenAMockedEnvironment()
        doAnswer(object : Answer<Any> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any? {
                (invocation.arguments[0] as GetUsers.Listener).onNoInternetAvailable()
                return null
            }
        }).`when`(mockErrorInteractor!!).getAsync(
                any(GetUsers.Listener::class.java))
        val presenter = mockContext?.let { UserListPresenter(it, mockErrorInteractor) }
        presenter!!.view = mockView!!
        return presenter
    }

    private fun givenAMockedPresenterWithBrokenAPI(): UserListPresenter {
        givenAMockedEnvironment()
        doAnswer(object : Answer<Any> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any? {
                (invocation.arguments[0] as GetUsers.Listener).onError(Exception("Unparseable JSON Message"))
                return null
            }
        }).`when`(mockErrorInteractor!!).getAsync(
                any(GetUsers.Listener::class.java))
        val presenter = mockContext?.let { UserListPresenter(it, mockErrorInteractor) }
        presenter!!.view = mockView!!
        return presenter
    }

    private fun givenAMockedUser(): User {
        return mock(User::class.java)
    }
}
