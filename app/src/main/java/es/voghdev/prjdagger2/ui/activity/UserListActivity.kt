package es.voghdev.prjdagger2.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.pedrogomez.renderers.ListAdapteeCollection
import com.pedrogomez.renderers.RVRendererAdapter
import java.util.ArrayList
import javax.inject.Inject
import butterknife.InjectView
import es.voghdev.prjdagger2.R
import es.voghdev.prjdagger2.global.App
import es.voghdev.prjdagger2.global.di.DaggerUserListComponent
import es.voghdev.prjdagger2.global.di.UserListComponent
import es.voghdev.prjdagger2.global.di.UserListModule
import es.voghdev.prjdagger2.global.model.User
import es.voghdev.prjdagger2.interactor.GetUsersInteractor
import es.voghdev.prjdagger2.ui.presenter.UserListContract
import es.voghdev.prjdagger2.ui.presenter.UserListPresenter
import es.voghdev.prjdagger2.ui.renderer.UserRenderer
import es.voghdev.prjdagger2.ui.renderer.UserRendererBuilder
import es.voghdev.prjdagger2.usecase.ShowUserClicked
import es.voghdev.prjdagger2.usecase.ShowUserGreeting

class UserListActivity : BaseActivity(), UserListContract.View {
    @InjectView(R.id.users_list)
    lateinit var recyclerView: RecyclerView

    @InjectView(R.id.users_progressBar)
    lateinit var progressBar: ProgressBar

    private lateinit var adapter: RVRendererAdapter<User>

    internal lateinit var presenter: UserListPresenter

    @Inject
    lateinit var getUsersInteractor: GetUsersInteractor

    @Inject
    lateinit var showUserClicked: ShowUserClicked
    @Inject
    lateinit var showUserGreeting: ShowUserGreeting
    private var component: UserListComponent? = null
    @InjectView(R.id.tvTitle)
    lateinit var tvTitle: TextView

    internal val mUserClickListener: UserRenderer.OnUserClicked = object : UserRenderer.OnUserClicked {
        override fun onPictureClicked(user: User) {
            presenter.onUserPictureClicked(user)
        }

        override fun onBackgroundClicked(user: User) {
            presenter.onUserRowClicked(user)
        }
    }

    protected override val layoutId: Int
        get() = R.layout.activity_users_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component()!!.inject(this)

        adapter = RVRendererAdapter(
                LayoutInflater.from(this),
                UserRendererBuilder(this, mUserClickListener),
                ListAdapteeCollection(ArrayList())
        )

        presenter = UserListPresenter(this, getUsersInteractor!!)
        presenter.attach(this)
        presenter.initialize()
        tvTitle.text = getString(R.string.app_name)

        if(supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun showUserList(users: List<User>) {
        for(u in users) {
            adapter.add(u)
        }

        adapter.notifyDataSetChanged()
    }

    override fun showUserListError(e: Exception) {
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
    }

    override fun showNoInternetMessage() {
        Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun makeUserSayHello(user: User) {
        showUserGreeting.show(user)
    }

    override fun showUserClickedMessage(user: User) {
        showUserClicked.show(user)
    }

    private fun component(): UserListComponent? {
        if(component == null) {
            component = DaggerUserListComponent.builder()
                    .rootComponent((application as App).component)
                    .userListModule(UserListModule(applicationContext))
                    .mainModule((application as App).mainModule)
                    .build()
        }
        return component
    }
}
