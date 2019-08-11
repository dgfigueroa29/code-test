package es.voghdev.prjdagger2.global.model

class User {
	
	var id = ""
	var name = ""
	var address = ""
	var username = ""
	var thumbnail: String? = ""
	var facebookId = ""
	var email = ""
	
	fun hasThumbnail(): Boolean {
		return thumbnail != null && !thumbnail!!.isEmpty()
	}
	
	override fun toString(): String {
		return "User{\nid: $id \n" +
			"name: $name \n" +
			"address: $address \n" +
			"username: $username \n" +
			"thumbnail: $thumbnail \n" +
			"email: $email}"
	}
}
