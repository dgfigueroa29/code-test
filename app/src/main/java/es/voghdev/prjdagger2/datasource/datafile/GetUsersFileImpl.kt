package es.voghdev.prjdagger2.datasource.datafile

import android.content.Context

import com.google.gson.Gson

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import java.util.Collections

import es.voghdev.prjdagger2.R
import es.voghdev.prjdagger2.datasource.api.GetUsersResponse
import es.voghdev.prjdagger2.datasource.api.model.UserApiEntry
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.interactor.Interactor
import es.voghdev.prjdagger2.interactor.MainThread
import es.voghdev.prjdagger2.interactor.impl.ThreadExecutor
import es.voghdev.prjdagger2.usecase.GetUsers
import java.io.BufferedReader
import java.lang.Thread.sleep

class GetUsersFileImpl(private val mContext: Context, private val threadExecutor: ThreadExecutor, private val mainThread: MainThread) : Interactor, GetUsers {
	private var listener: GetUsers.Listener = EmptyListener()
	
	@Throws(IOException::class)
	private fun parseUsersFromRawFile(resId: Int): List<User> {
		val inputStream = mContext.resources.openRawResource(resId)
		val reader = BufferedReader(inputStream.reader())
		val content = StringBuilder()
		try {
			var line = reader.readLine()
			while (line != null) {
				content.append(line)
				line = reader.readLine()
			}
		} finally {
			reader.close()
		}
		
		inputStream.close()
		val json = content.toString()
		return getUsersFromJson(json)
	}
	
	private fun getUsersFromJson(json: String): List<User> {
		val response = Gson().fromJson(json, GetUsersResponse::class.java)
		val users = ArrayList<User>()
		for(entry in response.results!!) {
			users.add(entry.parseUser())
		}
		return users
	}
	
	override fun get(): List<User> {
		try {
			return parseUsersFromRawFile(R.raw.users)
		} catch(e: IOException) {
			return emptyList()
		}
		
	}
	
	override fun getAsync(listener: GetUsers.Listener) {
		this.listener = listener
		this.threadExecutor.run(this)
		
	}
	
	override fun run() {
		try {
			val users = parseUsersFromRawFile(R.raw.users)
			sleep(3000)
			mainThread.post(Runnable { listener.onUsersReceived(users, true) })
		} catch(e: IOException) {
			listener.onError(e)
		} catch(e: InterruptedException) {
			listener.onError(e)
		}
		
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
