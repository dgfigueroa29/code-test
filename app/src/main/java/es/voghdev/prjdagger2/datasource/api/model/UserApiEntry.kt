package es.voghdev.prjdagger2.datasource.api.model

import es.voghdev.prjdagger2.global.model.User

class UserApiEntry {
	
	internal var id = IdApiEntry()
	internal var name = UserNameApiEntry()
	internal var email = ""
	internal var gender = ""
	internal var picture = UserPictureApiEntry()
	internal var location = UserLocationApiEntry()
	internal var md5 = ""
	internal var dob: UserDateOfBirthApiEntry? = null
	
	fun parseUser(): User {
		val u = User()
		u.id = id.parseId()
		u.email = email
		u.address = location.street
		u.name = String.format("%s %s %s", name.title, name.first, name.last)
		u.thumbnail = picture.thumbnail
		return u
	}
	
	companion object {
		
		private val SPACE = " "
	}
}
