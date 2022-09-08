package sgtmelon.scriptum.cleanup.presentation.screen.ui

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import sgtmelon.extensions.getColorAttr
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.extension.geDisplayedTheme
import sgtmelon.scriptum.cleanup.presentation.control.toast.ToastControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.IAppActivity
import sgtmelon.scriptum.infrastructure.model.key.Theme
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

/**
 * Parent class for all activities.
 */
abstract class ParentActivity : AppCompatActivity(), IAppActivity {

    val toastControl by lazy { ToastControl(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Make statusBar and navigationBar translucent.
         */
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    override fun onDestroy() {
        super.onDestroy()
        toastControl.onDestroy()
    }

    override fun setupTheme(theme: Theme) {
        val mode = when (theme) {
            Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            Theme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    /**
     * Set light statusBar and navigationBar from xml not working.
     */
    override fun changeControlColor() {
        val theme = geDisplayedTheme() ?: return
        val onLight = theme == ThemeDisplayed.LIGHT

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

    /**
     * Set system colors from xml not working.
     */
    override fun changeSystemColor() {
        val theme = geDisplayedTheme() ?: return

        setWindowBackground()
        setStatusBarColor()
        setNavigationColor(theme)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            setNavigationDividerColor(theme)
        }
    }

    protected open fun setWindowBackground() {
        window.setBackgroundDrawable(ColorDrawable(getColorAttr(R.attr.clBackgroundWindow)))
    }

    protected open fun setStatusBarColor() {
        window.statusBarColor = getColorAttr(R.attr.colorPrimaryDark)
    }

    protected open fun setNavigationColor(theme: ThemeDisplayed) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                window.navigationBarColor = getColorAttr(R.attr.colorPrimary)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                if (theme == ThemeDisplayed.LIGHT) {
                    window.navigationBarColor = getColorAttr(R.attr.clContentSecond)
                } else {
                    window.navigationBarColor = getColorAttr(R.attr.colorPrimary)
                }
            }
            else -> {
                if (theme == ThemeDisplayed.LIGHT) {
                    window.navigationBarColor = getColorAttr(R.attr.colorPrimaryDark)
                } else {
                    window.navigationBarColor = getColorAttr(R.attr.colorPrimary)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    protected open fun setNavigationDividerColor(theme: ThemeDisplayed) {
        window.navigationBarDividerColor = getColorAttr(R.attr.clDivider)
    }
}