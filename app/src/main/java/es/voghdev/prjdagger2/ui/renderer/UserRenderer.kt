package es.voghdev.prjdagger2.ui.renderer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.pedrogomez.renderers.Renderer
import com.squareup.picasso.Picasso
import java.util.Locale
import butterknife.ButterKnife
import butterknife.InjectView
import butterknife.OnClick
import es.voghdev.prjdagger2.R
import es.voghdev.prjdagger2.global.model.User

class UserRenderer internal constructor(ctx: Context, onUserClicked: OnUserClicked) : Renderer<User>() {
    private val mContext: Context = ctx.applicationContext
    private var listener: OnUserClicked = EmptyOnUserClicked()
    @InjectView(R.id.user_container)
    lateinit var root: RelativeLayout
    @InjectView(R.id.user_iv_background)
    lateinit var ivBackground : ImageView
    @InjectView(R.id.user_iv_thumbnail)
    lateinit var ivThumbnail: ImageView
    @InjectView(R.id.user_tv_title)
    lateinit var tvTitle: TextView
    @InjectView(R.id.user_tv_description)
    lateinit var tvDescription: TextView
    @InjectView(R.id.user_tv_label)
    lateinit var tvClicks: TextView

    init {
        setListener(onUserClicked)
    }

    interface OnUserClicked {
        fun onPictureClicked(user: User)
        fun onBackgroundClicked(user: User)
    }

    protected fun setListener(listener: OnUserClicked?) {
        if(listener != null) {
            this.listener = listener
        }
    }

    override fun setUpView(rootView: View) {
        ButterKnife.inject(this, rootView)
    }

    override fun hookListeners(rootView: View) {
    }

    @OnClick(R.id.user_container)
    internal fun onClickRow(root: RelativeLayout) {
        listener.onBackgroundClicked(content)
    }

    @OnClick(R.id.user_iv_thumbnail)
    internal fun onClickThumbnail(imageView: ImageView) {
        listener.onPictureClicked(content)
    }

    override fun inflate(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.row_user, parent, false)
    }

    override fun render() {
        val u = content
        renderTitle(u)
        renderDescription(u)
        renderBackground(u)
        renderThumbnail(u)
        renderClicks(0)
    }

    private fun renderBackground(u: User) {
        val resId = R.mipmap.background3
        Picasso.get()
                .load(resId)
                .into(ivBackground)
    }

    private fun renderThumbnail(user: User) {
        if(!user.hasThumbnail()) {
            return
        }

        Picasso.get()
                .load(user.thumbnail).error(R.mipmap.background1)
                .resizeDimen(R.dimen.user_thumbnail_w, R.dimen.user_thumbnail_h)
                .placeholder(R.mipmap.background1)
                .into(ivThumbnail)
    }

    private fun renderDescription(user: User) {
        tvDescription!!.text = user.email
    }

    private fun renderTitle(user: User) {
        tvTitle!!.text = user.name
    }

    private fun renderClicks(numberOfClicks: Int) {
        tvClicks!!.text = String.format(Locale.getDefault(), "%d", numberOfClicks)
    }

    private inner class EmptyOnUserClicked : OnUserClicked {
        override fun onPictureClicked(user: User) {
            /* Empty */
        }

        override fun onBackgroundClicked(user: User) {
            /* Empty */
        }
    }
}