package es.voghdev.prjdagger2.repository.cachepolicy

class TimedCachePolicy(millis: Long) : CachePolicy {
	
	internal var start: Long = 0
	internal var expiration: Long = 0
	
	override val isCacheValid: Boolean
		get() = System.currentTimeMillis() <= start + expiration
	
	init {
		start = System.currentTimeMillis()
		expiration = millis
	}
	
	companion object {
		protected val MS_PER_MINUTE: Long = 60000
		val ONE_MINUTE = MS_PER_MINUTE
	}
}
