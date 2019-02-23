package sgtmelon.scriptum.app.screen.parent

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R

abstract class ParentActivity : AppCompatActivity(), ParentCallback{

    private val viewModel: ParentViewModel by lazy {
        ViewModelProviders.of(this).get(ParentViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        checkThemeChange()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.callback = this
        viewModel.onSetupTheme()
    }

    fun checkThemeChange() {
        if (!viewModel.isThemeChange()) return

        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        startActivity(intent)
    }

}