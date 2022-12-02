package sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint

import android.content.Context
import sgtmelon.scriptum.cleanup.extension.getNoteToolbarColor
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Parent class for control tint.
 */
abstract class ParentTintControl(protected val context: Context) {

    protected fun getToolbarColor(theme: ThemeDisplayed, color: Color): Int {
        return context.getNoteToolbarColor(theme, color, needDark = false)
    }

    protected fun getStatusBarColor(theme: ThemeDisplayed, color: Color): Int {
        return context.getNoteToolbarColor(theme, color, needDark = false)
    }
}