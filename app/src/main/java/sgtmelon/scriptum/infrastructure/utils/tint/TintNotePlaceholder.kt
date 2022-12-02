package sgtmelon.scriptum.infrastructure.utils.tint

import android.content.Context
import android.view.View
import android.view.Window
import sgtmelon.scriptum.cleanup.extension.getDisplayedTheme
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Control colors of note toolbar placeholder.
 */
class TintNotePlaceholder(context: Context) : TintNoteBar(context) {

    private val theme: ThemeDisplayed? get() = context.getDisplayedTheme()

    fun changeColor(color: Color, window: Window, holder: View?) {
        val theme = theme?.takeIf { it != ThemeDisplayed.DARK } ?: return

        window.statusBarColor = getStatusBarColor(theme, color)
        holder?.setBackgroundColor(getToolbarColor(theme, color))
    }
}