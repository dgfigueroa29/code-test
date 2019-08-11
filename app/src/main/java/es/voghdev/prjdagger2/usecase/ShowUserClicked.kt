package es.voghdev.prjdagger2.usecase

import es.voghdev.prjdagger2.global.model.User

interface ShowUserClicked {
    fun show(user: User)
}
