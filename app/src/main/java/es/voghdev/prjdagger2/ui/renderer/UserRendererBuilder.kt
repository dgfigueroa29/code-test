package es.voghdev.prjdagger2.ui.renderer

import android.content.Context
import com.pedrogomez.renderers.Renderer
import com.pedrogomez.renderers.RendererBuilder
import java.util.LinkedList
import es.voghdev.prjdagger2.global.model.User

class UserRendererBuilder(context: Context, onUserClicked: UserRenderer.OnUserClicked) : RendererBuilder<User>() {
    init {
        val prototypes = getPrototypes(context, onUserClicked)
        setPrototypes(prototypes)
    }

    override fun getPrototypeClass(content: User): Class<*> {
        return UserRenderer::class.java
    }

    private fun getPrototypes(context: Context, onUserClicked: UserRenderer.OnUserClicked): List<Renderer<User>> {
        val prototypes = LinkedList<Renderer<User>>()
        val userRenderer = UserRenderer(context, onUserClicked)
        prototypes.add(userRenderer)
        return prototypes
    }
}