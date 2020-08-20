package sgtmelon.scriptum.presentation.screen.ui.impl

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import sgtmelon.idling.AppIdlingResource
import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.ui.ParentActivity
import sgtmelon.scriptum.presentation.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel
import javax.inject.Inject

/**
 * Parent activity for application, which need extends when need change theme.
 */
abstract class AppActivity : ParentActivity(), IAppActivity {

    @Inject internal lateinit var appViewModel: IAppViewModel

    protected val fm get() = supportFragmentManager

    /**
     * For children call [onCreate] super after injecting configure.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel.onSetup()
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
        appViewModel.onDestroy()
    }

    /**
     * Set light statusBar and navigationBar from xml not working.
     */
    override fun changeControlColor(onLight: Boolean) {
        changeStatusControl(onLight)
        changeNavigationControl(onLight)
    }

    private fun changeStatusControl(onLight: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        if (onLight) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

    private fun changeNavigationControl(onLight: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        if (onLight) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }
    }

    fun checkThemeChange() {
        if (!appViewModel.isThemeChange()) return

        val intent = intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        startActivity(intent)
    }

}