package sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint

import android.content.Context
import android.view.View
import android.view.Window
import sgtmelon.scriptum.cleanup.extension.getDisplayedTheme
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Control note toolbar holder color.
 */
class HolderTintControl(
    context: Context,
    private val window: Window,
    private val holder: View?
) : ParentTintControl(context) {

    private val theme: ThemeDisplayed? = context.getDisplayedTheme()

    fun setupColor(color: Color) {
        if (theme == null || theme == ThemeDisplayed.DARK) return

        window.statusBarColor = getStatusBarColor(theme, color)

        holder?.setBackgroundColor(getToolbarColor(theme, color))
    }
}