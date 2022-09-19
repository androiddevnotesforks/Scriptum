package sgtmelon.scriptum.infrastructure.system.delegators.window

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.ColorInt
import sgtmelon.extensions.getColorAttr
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.extension.getDisplayedTheme
import sgtmelon.scriptum.cleanup.extension.isPortraitMode
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.Background
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.NavDivider
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.Navigation
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys.StatusBar

/**
 * Class for simple setup of android bars color and window background.
 */
class WindowUiDelegator(
    private val window: Window,
    private val background: Background,
    private val statusBar: StatusBar,
    private val navigation: Navigation,
    private val navDivider: NavDivider
) {

    fun setup(context: Context) {
        val theme = context.getDisplayedTheme() ?: return

        /** Makes statusBar and navigationBar translucent. */
        toTranslucent()

        /** Set system bars color via xml not working. */
        setupBackground(context)
        setupStatusBar(context)
        setupNavigation(context, theme)
        setupNavDivider(context)

        /** Set light statusBar and navigationBar icons not working from xml. */
        setupIcons(theme)
    }

    private fun toTranslucent() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    //region Setup background and bar's color

    private fun setupBackground(context: Context) {
        val colorAttr = when (background) {
            Background.Standard -> R.attr.clBackgroundWindow
            Background.Dark -> R.attr.colorPrimaryDark
        }

        window.setBackgroundDrawable(ColorDrawable(context.getColorAttr(colorAttr)))
    }

    private fun setupStatusBar(context: Context) {
        val color = when (statusBar) {
            StatusBar.Standard -> context.getColorAttr(R.attr.colorPrimaryDark)
            StatusBar.Transparent -> Color.TRANSPARENT
        }

        window.statusBarColor = color
    }

    private fun setupNavigation(context: Context, theme: ThemeDisplayed) = with(context) {
        val color = when (navigation) {
            Navigation.Standard -> getNavigationStandard(context, theme)
            Navigation.RotationCatch -> {
                /** Makes color half-translucent in portrait orientation. */
                if (isPortraitMode()) {
                    getColorAttr(R.attr.clNavigationBar)
                } else {
                    getNavigationStandard(context, theme)
                }
            }
            Navigation.Transparent -> Color.TRANSPARENT
        }

        window.navigationBarColor = color
    }

    @ColorInt
    private fun getNavigationStandard(context: Context, theme: ThemeDisplayed): Int {
        /** This selection related with ability to change buttons inside navigation bar. */
        val colorAttr = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> R.attr.colorPrimary
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> when (theme) {
                ThemeDisplayed.LIGHT -> R.attr.clContentSecond
                ThemeDisplayed.DARK -> R.attr.colorPrimary
            }
            else -> when (theme) {
                ThemeDisplayed.LIGHT -> R.attr.colorPrimaryDark
                ThemeDisplayed.DARK -> R.attr.colorPrimary
            }
        }

        return context.getColorAttr(colorAttr)
    }

    private fun setupNavDivider(context: Context) = with(context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return

        val color = when (navDivider) {
            NavDivider.Standard -> getColorAttr(R.attr.clDivider)
            NavDivider.RotationCatch -> {
                /** Makes color half-translucent in portrait orientation. */
                val attr = if (isPortraitMode()) {
                    R.attr.clNavigationBarDivider
                } else {
                    R.attr.clDivider
                }

                getColorAttr(attr)
            }
            NavDivider.Transparent -> Color.TRANSPARENT
        }

        window.navigationBarDividerColor = color
    }

    //endregion

    //region Change icons color

    private fun setupIcons(theme: ThemeDisplayed) {
        val onLight = when (theme) {
            ThemeDisplayed.LIGHT -> true
            ThemeDisplayed.DARK -> false
        }

        changeStatusControl(onLight)
        changeNavigationControl(onLight)
    }

    private fun changeStatusControl(onLight: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        val uiVisibility = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = if (onLight) {
            uiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            uiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

    private fun changeNavigationControl(onLight: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val uiVisibility = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = if (onLight) {
            uiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            uiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }
    }

    //endregion

}