package sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint

import android.content.Context
import android.os.Build
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme
import sgtmelon.scriptum.cleanup.extension.getNoteToolbarColor

/**
 * Parent class for control tint.
 */
abstract class ParentTintControl(protected val context: Context) {

    protected val statusOnDark = Build.VERSION.SDK_INT < Build.VERSION_CODES.M

    protected fun getToolbarColor(@Theme theme: Int, @Color color: Int): Int {
        return context.getNoteToolbarColor(theme, color, needDark = false)
    }

    protected fun getStatusBarColor(@Theme theme: Int, @Color color: Int): Int {
        return context.getNoteToolbarColor(theme, color, statusOnDark)
    }

}