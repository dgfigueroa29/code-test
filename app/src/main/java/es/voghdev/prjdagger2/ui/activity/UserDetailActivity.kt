package es.voghdev.prjdagger2.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import javax.inject.Inject
import butterknife.InjectView
import de.hdodenhof.circleimageview.CircleImageView
import es.voghdev.prjdagger2.R
import es.voghdev.prjdagger2.global.App
import es.voghdev.prjdagger2.global.di.DaggerUserDetailComponent
import es.voghdev.prjdagger2.global.di.UserDetailComponent
import es.voghdev.prjdagger2.global.di.UserDetailModule
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.interactor.ShowUserInteractor
import es.voghdev.prjdagger2.ui.presenter.UserDetailContract
import es.voghdev.prjdagger2.ui.presenter.UserDetailPresenter

class UserDetailActivity : BaseActivity(), UserDetailContract.View {
    @InjectView(R.id.tvName)
    lateinit var tvName: TextView
    @InjectView(R.id.tvEmail)
    lateinit var tvEmail: TextView
    @InjectView(R.id.ivImage)
    lateinit var ivImage: CircleImageView
    internal lateinit var presenter: UserDetailPresenter
    @Inject
    lateinit var interactor: ShowUserInteractor
    private var component: UserDetailComponent? = null
    @InjectView(R.id.ibBack)
    lateinit var ibBack: ImageButton
    @InjectView(R.id.tvTitle)
    lateinit var tvTitle: TextView
    @InjectView(R.id.tvAddress)
    lateinit var tvAddress: TextView

    override val layoutId: Int
        get() = R.layout.activity_users_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component()!!.inject(this)
        presenter = UserDetailPresenter(this, interactor!!)
        presenter.attach(this)
        presenter.initialize()
        ibBack.visibility = ImageButton.VISIBLE
        initialize()
    }

    private fun initialize() {
        try {
            val json = getSharedPreferences("boa", Context.MODE_PRIVATE).getString("user", "")
            val user = Gson().fromJson(json, User::class.java)
            tvName.text = user.name
            tvEmail.text = user.email
            tvAddress.text = user.address
            Picasso.get()
                    .load(user.thumbnail).error(R.mipmap.background1)
                    .resizeDimen(R.dimen.user_thumbnail_w, R.dimen.user_thumbnail_h)
                    .placeholder(R.mipmap.background1)
                    .into(ivImage)
            tvTitle.text = user.name
            ibBack.setOnClickListener { onBackPressed() }
        } catch(e: Exception) {
            showUserError(e)
        }
    }

    override fun showUser(user: User) {}

    override fun showUserError(e: Exception) {
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
    }

    override fun showNoInternetMessage() {
        Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {}

    override fun hideLoading() {}

    private fun component(): UserDetailComponent? {
        if(component == null) {
            component = DaggerUserDetailComponent.builder()
                    .rootComponent((application as App).component)
                    .userDetailModule(UserDetailModule(applicationContext))
                    .mainModule((application as App).mainModule)
                    .build()
        }
        return component
    }

    override fun onBackPressed() {
        getSharedPreferences("boa", Context.MODE_PRIVATE).edit()
                .putString("user", "").apply()
        startActivity(Intent(this, UserListActivity::class.java))
        finish()
    }
}
