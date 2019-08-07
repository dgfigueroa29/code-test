package es.voghdev.prjdagger2.ui.view

import android.content.Context
import android.widget.Toast

import es.voghdev.prjdagger2.R
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.usecase.ShowUserGreeting

/**
 * Created by olmo on 31/10/16.
 */
class ShowUserGreetingToastImpl(internal var context: Context) : ShowUserGreeting {
	
	override fun show(user: User) {
		Toast.makeText(context, context.getString(R.string.user_greeting, user.name),
				Toast.LENGTH_LONG).show()
	}
}
