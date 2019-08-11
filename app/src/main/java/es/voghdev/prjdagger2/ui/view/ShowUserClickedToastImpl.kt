package es.voghdev.prjdagger2.ui.view

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.ui.activity.UserDetailActivity
import es.voghdev.prjdagger2.usecase.ShowUserClicked

class ShowUserClickedToastImpl(internal var context: Context) : ShowUserClicked {
	override fun show(user: User) {
		val json = Gson().toJson(user)
		//Persist temp user
		context.getSharedPreferences("boa",Context.MODE_PRIVATE).edit()
			.putString("user", json).apply()
		val intent = Intent(context, UserDetailActivity::class.java)
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
		context.startActivity(intent)
	}
}