package es.voghdev.prjdagger2.repository.cachepolicy

class NoCachePolicy : CachePolicy {
	override val isCacheValid: Boolean
		get() = false
}
