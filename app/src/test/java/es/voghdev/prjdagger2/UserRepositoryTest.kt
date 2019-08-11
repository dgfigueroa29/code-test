/*
 * Copyright (C) 2016 Olmo Gallegos Hernández.
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
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import java.util.ArrayList
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.repository.UserRepository
import es.voghdev.prjdagger2.repository.cachepolicy.NoCachePolicy
import es.voghdev.prjdagger2.repository.cachepolicy.TimedCachePolicy
import es.voghdev.prjdagger2.usecase.GetUsers
import junit.framework.Assert.assertEquals
import org.mockito.Matchers.any
import org.mockito.Mockito.verify
import org.mockito.internal.verification.VerificationModeFactory.times

class UserRepositoryTest : BaseUnitTest() {
    @Mock
    internal var mockGetUsersFile: GetUsers? = null
    @Mock
    internal var mockGetUsersApi: GetUsers? = null
    @Mock
    internal var mockContext: Context? = null
    @Mock
    internal var userListCollaborator: UserListCollaborator? = null

    internal var repositoryCaller: RepositoryCaller? = null

    @Captor
    internal var argumentCaptor: ArgumentCaptor<GetUsers.Listener>? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    @Throws(Exception::class)
    fun shouldUseApiDataSourceForGettingUsers() {
        val userRepository = givenAMockedRepository()
        userRepository.getAsync(EmptyListener())
        verify(mockGetUsersApi!!, times(1)).getAsync(any(GetUsers.Listener::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnTwoUsersWhenUsingApiDataSource() {
        givenAMockedRepository()
        repositoryCaller = RepositoryCaller(mockGetUsersApi)
        repositoryCaller!!.getUsers()
        verify(mockGetUsersApi!!, times(1)).getAsync(argumentCaptor!!.capture())
        verify(mockGetUsersFile!!, times(0)).getAsync(any(GetUsers.Listener::class.java))
        assertEquals(2, repositoryCaller!!.result.size)
    }

    @Test
    @Throws(Exception::class)
    fun shouldReturnOneUserWhenUsingFileDataSource() {
        givenAMockedRepository()
        repositoryCaller = RepositoryCaller(mockGetUsersFile)
        repositoryCaller!!.getUsers()
        verify(mockGetUsersFile!!, times(1)).getAsync(argumentCaptor!!.capture())
        verify(mockGetUsersApi!!, times(0)).getAsync(any(GetUsers.Listener::class.java))
        assertEquals(1, repositoryCaller!!.result.size)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotUseFileDataSourceForGettingUsers() {
        val userRepository = givenAMockedRepository()
        userRepository.getAsync(EmptyListener())
        verify(mockGetUsersFile!!, times(0)).getAsync(any(GetUsers.Listener::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotUseAnyDataSourceInTheSecondCall() {
        val userRepository = givenAMockedRepository()
        userRepository.get()
        verify(mockGetUsersApi!!, times(1)).get()
        userRepository.get()
        verify(mockGetUsersApi!!, times(1)).get() // Interactions should remain 1
    }

    @Test
    @Throws(Exception::class)
    fun shouldAlwaysUseApiDataSourceIfCachePolicySaysAlwaysInvalidate() {
        val userRepository = givenAMockedRepository()
        userRepository.setCachePolicy(NoCachePolicy())
        userRepository.getAsync(EmptyListener())
        verify(mockGetUsersApi!!, times(1)).getAsync(any(GetUsers.Listener::class.java))
        userRepository.getAsync(EmptyListener())
        verify(mockGetUsersApi!!, times(2)).getAsync(any(GetUsers.Listener::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotUseAnyDataSourceInTheSecondAsynchronousCall() {
        val userRepository = givenAMockedRepository()
        userRepository.setCachePolicy(TimedCachePolicy(20000))
        userRepository.getAsync(EmptyListener())
        verify(mockGetUsersApi!!, times(1)).getAsync(any(GetUsers.Listener::class.java))
        userRepository.getAsync(EmptyListener())
        verify(mockGetUsersApi!!, times(1)).getAsync(any(GetUsers.Listener::class.java))
    }

    private fun givenAMockedRepository(): UserRepository {
        val apiUsers = givenThereAreSomeUsers()
        val fileUsers = givenThereIsOneUser()
        val userRepository = UserRepository(mockContext!!, mockGetUsersApi!!, mockGetUsersFile!!)
        Mockito.doAnswer(object : Answer<Any> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any? {
                (invocation.arguments[0] as GetUsers.Listener).onUsersReceived(apiUsers, true)
                return null
            }
        }).`when`(mockGetUsersApi!!).getAsync(any(GetUsers.Listener::class.java))

        Mockito.doAnswer(object : Answer<Any> {
            @Throws(Throwable::class)
            override fun answer(invocation: InvocationOnMock): Any? {
                (invocation.arguments[0] as GetUsers.Listener).onUsersReceived(fileUsers, true)
                return null
            }
        }).`when`(mockGetUsersFile!!).getAsync(any(GetUsers.Listener::class.java))
        Mockito.`when`(mockGetUsersApi!!.get()).thenReturn(apiUsers)
        Mockito.`when`(mockGetUsersFile!!.get()).thenReturn(fileUsers)
        return userRepository
    }

    protected fun givenThereAreSomeUsers(): List<User> {
        val list = ArrayList<User>()
        list.add(createMockUser("005-271", "Philip Borke", "St. Etienne 57",
                "ph@some-company.com", BaseUnitTest.SAMPLE_AVATAR, "12345678"))
        list.add(createMockUser("005-272", "Andre Lozano", "St. Etienne 58",
                "alzn@some-company.com", BaseUnitTest.SAMPLE_AVATAR, "12345679"))
        return list
    }

    protected fun givenThereIsOneUser(): List<User> {
        val list = ArrayList<User>()
        list.add(createMockUser("005-273", "Anna Ivanova", "St. Etienne 61",
                "anna@some-company.com", BaseUnitTest.SAMPLE_AVATAR, "12345677"))
        return list
    }

    private inner class EmptyListener : GetUsers.Listener {
        override fun onUsersReceived(users: List<User>, isCached: Boolean) {
            /* Empty */
        }

        override fun onError(e: Exception) {
            /* Empty */
        }

        override fun onNoInternetAvailable() {
            /* Empty */
        }
    }
}
