package es.voghdev.prjdagger2.interactor

interface MainThread {
	fun post(runnable: Runnable)
}