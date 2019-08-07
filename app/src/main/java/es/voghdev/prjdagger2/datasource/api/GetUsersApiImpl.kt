package es.voghdev.prjdagger2.datasource.api

import java.util.ArrayList

import es.voghdev.prjdagger2.datasource.api.model.UserApiEntry
import es.voghdev.prjdagger2.datasource.api.retrofit.GetUsersRetrofitRequest
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.usecase.GetUsers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GetUsersApiImpl(private val pageSize: Int, private val pageNumber: Int) : GetUsers, Callback<GetUsersResponse> {
	
	private var listener: GetUsers.Listener = NullListener()
	
	override fun get(): List<User> {
		throw IllegalStateException("Not implemented yet")
	}
	
	override fun getAsync(listener: GetUsers.Listener?) {
		if(listener != null) {
			this.listener = listener
		}
		val retrofit = Retrofit.Builder()
				.baseUrl(ENDPOINT)
				.addConverterFactory(GsonConverterFactory.create())
				.build()
		val request = retrofit.create(GetUsersRetrofitRequest::class.java)
		val call = request.getRandomUsers(pageSize, pageNumber)
		call.enqueue(this)
	}
	
	override fun onResponse(call: Call<GetUsersResponse>, response: Response<GetUsersResponse>) {
		val users = ArrayList<User>()
		
		for(entry in response.body()!!.results!!) {
			users.add(entry.parseUser())
		}
		
		listener.onUsersReceived(users, false)
	}
	
	override fun onFailure(call: Call<GetUsersResponse>, t: Throwable) {
		listener.onError(Exception(t))
	}
	
	private inner class NullListener : GetUsers.Listener {
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
	
	companion object {
		private val ENDPOINT = "https://api.randomuser.me/"
	}
}
