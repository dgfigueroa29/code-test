package es.voghdev.prjdagger2.interactor

import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.usecase.GetUsers

class GetUsersInteractor(internal var getUsers: GetUsers, internal var executor: Executor, internal var mainThread: MainThread) : Interactor, GetUsers, GetUsers.Listener {
	internal var listener: GetUsers.Listener = NullListener()
	
	override fun onUsersReceived(list: List<User>, isCached: Boolean) {
		listener.onUsersReceived(list, isCached)
	}
	
	override fun onError(e: Exception) {
		listener.onError(e)
	}
	
	override fun onNoInternetAvailable() {
		listener.onNoInternetAvailable()
	}
	
	override fun run() {
		getUsers.getAsync(listener)
	}
	
	override fun get(): List<User> {
		throw IllegalArgumentException("Please use async version of this Interactor")
	}
	
	override fun getAsync(listener: GetUsers.Listener?) {
		if(listener != null) {
			this.listener = listener
		}
		this.executor.run(this)
	}
	
	private inner class NullListener : GetUsers.Listener {
		override fun onUsersReceived(list: List<User>, isCached: Boolean) {}
		
		override fun onError(e: Exception) {}
		
		override fun onNoInternetAvailable() {}
	}
}
