package sgtmelon.scriptum.presentation.screen.ui

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.extension.getColorAttr

/**
 * Parent class for all activities.
 */
abstract class ParentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Make statusBar and navigationBar translucent.
         */
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    /**
     * Set light statusBar and navigationBar from xml not working.
     */
    fun changeControlColor(onLight: Boolean) {
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
    fun changeSystemColor(@Theme theme: Int) {
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

}