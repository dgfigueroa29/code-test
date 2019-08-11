package es.voghdev.prjdagger2.global

import android.app.Application
import android.support.annotation.VisibleForTesting
import com.squareup.picasso.Picasso
import java.io.File
import es.voghdev.prjdagger2.global.di.DaggerRootComponent
import es.voghdev.prjdagger2.global.di.MainModule
import es.voghdev.prjdagger2.global.di.RootComponent
import es.voghdev.prjdagger2.ui.picasso.PicassoImageCache

class App : Application() {
    @set:VisibleForTesting
    var component: RootComponent? = null
    private var cache: PicassoImageCache? = null
    var mainModule: MainModule? = null
        private set

    private val picturesDir: File?
        get() = getExternalFilesDir(IMAGES_DIR)

    override fun onCreate() {
        super.onCreate()
        initializeDependencyInjector()
        initializeImageCache()
    }

    private fun initializeDependencyInjector() {
        mainModule = MainModule(this)
        component = DaggerRootComponent.builder().mainModule(mainModule).build()
        component!!.inject(this)
    }

    private fun initializeImageCache() {
        val cacheDir = picturesDir ?: return
        cacheDir.mkdirs()
        cache = PicassoImageCache(this, cacheDir)
        val picasso = Picasso.Builder(this).memoryCache(cache!!).build()
    }

    companion object {
        const val IMAGES_DIR = "images"
    }
}
