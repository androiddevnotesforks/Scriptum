package sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint

import android.content.Context
import android.os.Build
import sgtmelon.scriptum.cleanup.extension.getNoteToolbarColor
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Parent class for control tint.
 */
abstract class ParentTintControl(protected val context: Context) {

    protected val statusOnDark = Build.VERSION.SDK_INT < Build.VERSION_CODES.M

    protected fun getToolbarColor(theme: ThemeDisplayed, color: Color): Int {
        return context.getNoteToolbarColor(theme, color, needDark = false)
    }

    protected fun getStatusBarColor(theme: ThemeDisplayed, color: Color): Int {
        return context.getNoteToolbarColor(theme, color, statusOnDark)
    }
}