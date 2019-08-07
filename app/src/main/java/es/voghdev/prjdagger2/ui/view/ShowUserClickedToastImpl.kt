package es.voghdev.prjdagger2.ui.view

import android.content.Context
import android.widget.Toast

import es.voghdev.prjdagger2.R
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.usecase.ShowUserClicked

class ShowUserClickedToastImpl(internal var context: Context) : ShowUserClicked {
	
	override fun show(user: User) {
		Toast.makeText(context, context.getString(R.string.user_was_clicked, user.email),
				Toast.LENGTH_LONG).show()
	}
}
