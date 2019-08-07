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

import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.interactor.GetUsersInteractor
import es.voghdev.prjdagger2.ui.presenter.UserListPresenter
import es.voghdev.prjdagger2.usecase.GetUsers

class PresenterCaller(internal var collaborator: GetUsersInteractor) : GetUsers.Listener {
	var view: UserListPresenter.View? = null
		internal set
	
	fun getUsers() {
		collaborator.getAsync(this)
	}
	
	override fun onUsersReceived(users: List<User>, isCached: Boolean) {
	
	}
	
	override fun onError(e: Exception) {
		/* Empty */
	}
	
	override fun onNoInternetAvailable() {
		/* Empty */
	}
}
