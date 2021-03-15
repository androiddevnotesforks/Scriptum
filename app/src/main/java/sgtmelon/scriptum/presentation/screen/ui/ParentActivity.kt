package sgtmelon.scriptum.presentation.screen.ui

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.extension.getAppTheme
import sgtmelon.scriptum.extension.getColorAttr
import sgtmelon.scriptum.presentation.screen.ui.callback.IAppActivity

/**
 * Parent class for all activities.
 */
abstract class ParentActivity : AppCompatActivity(), IAppActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Make statusBar and navigationBar translucent.
         */
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    override fun setupTheme(@Theme theme: Int) {
        val mode = when (theme) {
            Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            Theme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    /**
     * Set light statusBar and navigationBar from xml not working.
     */
    override fun changeControlColor() {
        val theme = getAppTheme() ?: return
        val onLight = theme == Theme.LIGHT

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
        val theme = getAppTheme() ?: return

        setWindowBackground()
        setStatusBarColor()
        setNavigationColor(theme)
        setNavigationDividerColor(theme)
    }

    protected open fun setWindowBackground() {
        window.setBackgroundDrawable(ColorDrawable(getColorAttr(R.attr.clBackgroundWindow)))
    }

    protected open fun setStatusBarColor() {
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

}