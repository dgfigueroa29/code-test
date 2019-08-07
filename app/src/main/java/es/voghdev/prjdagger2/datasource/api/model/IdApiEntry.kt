package es.voghdev.prjdagger2.datasource.api.model


internal class IdApiEntry {
	private val name = ""
	private val value = ""
	
	fun parseId(): String {
		return String.format("%s%s", name, value)
	}
}
