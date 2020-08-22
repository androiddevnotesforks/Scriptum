package sgtmelon.scriptum.presentation.screen.ui.impl

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import sgtmelon.idling.AppIdlingResource
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.extension.getColorAttr
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

    override fun changeSystemColor(@Theme theme: Int) {
        setWindowBackground(theme)
        setStatusBarColor(theme)
        setNavigationColor(theme)
        setNavigationDividerColor(theme)
    }

    protected open fun setWindowBackground(@Theme theme: Int) {
        window.setBackgroundDrawable(ColorDrawable(getColorAttr(R.attr.clBackgroundWindow)))
    }

    protected open fun setStatusBarColor(@Theme theme: Int) {
        window.statusBarColor = getColorAttr(R.attr.colorPrimaryDark)
    }

    protected open fun setNavigationColor(@Theme theme: Int) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                window.navigationBarColor = getColorAttr(R.attr.colorPrimary)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                if (theme == Theme.LIGHT) {
                    window.navigationBarColor = getColorAttr(R.attr.clContentSecond)
                } else {
                    window.navigationBarColor = getColorAttr(R.attr.colorPrimary)
                }
            }
            else -> {
                if (theme == Theme.LIGHT) {
                    window.navigationBarColor = getColorAttr(R.attr.colorPrimaryDark)
                } else {
                    window.navigationBarColor = getColorAttr(R.attr.colorPrimary)
                }
            }
        }
    }

    protected open fun setNavigationDividerColor(@Theme theme: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = getColorAttr(R.attr.clDivider)
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