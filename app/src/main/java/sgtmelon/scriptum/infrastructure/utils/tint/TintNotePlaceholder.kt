package sgtmelon.scriptum.infrastructure.utils.tint

import android.content.Context
import android.view.View
import android.view.Window
import sgtmelon.scriptum.cleanup.extension.getDisplayedTheme
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Control note toolbar placeholder color.
 */
class TintNotePlaceholder(context: Context, private val window: Window) : TintNoteBar(context) {

    private val theme: ThemeDisplayed? = context.getDisplayedTheme()

    fun changeColor(color: Color, holder: View?) {
        val theme = theme?.takeIf { it != ThemeDisplayed.DARK } ?: return

        window.statusBarColor = getStatusBarColor(theme, color)
        holder?.setBackgroundColor(getToolbarColor(theme, color))
    }
}