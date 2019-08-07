/*
 * Copyright (C) 2015 Olmo Gallegos Hernández.
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

import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

import java.util.ArrayList

import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.usecase.GetUsers

import junit.framework.Assert.assertNull
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.mockito.Matchers.any
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class UserListTest : BaseUnitTest() {
	@Mock
	internal var userListCollaborator: UserListCollaborator? = null
	
	internal var userListCaller: UserListCaller
	
	@Captor
	internal var argumentCaptor: ArgumentCaptor<GetUsers.Listener>? = null
	
	@Before
	fun setUp() {
		MockitoAnnotations.initMocks(this)
		userListCaller = UserListCaller(userListCollaborator)
	}
	
	@Test
	@Throws(Exception::class)
	fun shouldAddTwoPlusTwo() {
		assertEquals(4, (2 + 2).toLong())
	}
	
	@Test
	@Throws(Exception::class)
	fun shouldReturnAMockedListOfUsers() {
		doAnswer(object : Answer {
			@Throws(Throwable::class)
			override fun answer(invocation: InvocationOnMock): Any? {
				val users = ArrayList<User>()
				val b = createMockUser("A001", "John Smith", "Sunset Blvd. 27", "smithjohn", "", "1248234823")
				users.add(b)
				(invocation.arguments[0] as GetUsers.Listener).onUsersReceived(users, true)
				return null
			}
		}).`when`(userListCollaborator).getUsers(
				any(GetUsers.Listener::class.java))
		
		userListCaller.getUsers()
		
		verify(userListCollaborator, times(1)).getUsers(
				any(GetUsers.Listener::class.java))
		
		assertEquals(userListCaller.result.size.toLong(), 1)
		assertEquals(userListCaller.result[0].name, "John Smith")
	}
	
	@Test
	@Throws(Exception::class)
	fun shouldReturnAMockedListOfUsersUsingAnArgumentCaptor() {
		userListCaller.getUsers()
		
		val result = ArrayList<User>()
		val a = createMockUser("A001", "John Smith", "Sunset Blvd. 27", "smithjohn", "", "1248234823")
		result.add(a)
		
		// Let's call the callback. ArgumentCaptor.capture() works like a matcher.
		verify(userListCollaborator, times(1)).getUsers(
				argumentCaptor!!.capture())
		
		assertNull(userListCaller.result)
		
		// Once you're satisfied, trigger the reply on callbackCaptor.getValue().
		argumentCaptor!!.value.onUsersReceived(result, true)
		
		// Some assertion about the state after the callback is called
		assertThat(userListCaller.result.size, `is`(equalTo(1)))
	}
}
