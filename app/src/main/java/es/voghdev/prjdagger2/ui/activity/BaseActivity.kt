package es.voghdev.prjdagger2.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import butterknife.ButterKnife

abstract class BaseActivity : AppCompatActivity() {
	
	protected abstract val layoutId: Int
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		setContentView(layoutId)
		
		ButterKnife.inject(this)
	}
}
