package sgtmelon.scriptum.screen.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sgtmelon.idling.AppIdlingResource
import sgtmelon.scriptum.R
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.screen.ui.callback.IAppActivity

/**
 * Parent activity for application, which need extends when need change theme
 */
abstract class AppActivity : AppCompatActivity(), IAppActivity {

    private val iViewModel by lazy { ViewModelFactory.get(activity = this) }

    protected val fm get() = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iViewModel.onSetup()
    }

    override fun onResume() {
        super.onResume()
        checkThemeChange()
    }

    override fun onStart() {
        super.onStart()
        AppIdlingResource.worker.stopHardWork()
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
    }

    fun checkThemeChange() {
        if (!iViewModel.isThemeChange()) return

        val intent = intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        startActivity(intent)
    }

}