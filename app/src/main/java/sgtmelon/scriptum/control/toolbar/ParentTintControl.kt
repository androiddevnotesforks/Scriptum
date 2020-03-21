package sgtmelon.scriptum.control.toolbar

import android.content.Context
import android.os.Build
import sgtmelon.scriptum.extension.getAppThemeColor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Parent class for control tint.
 */
abstract class ParentTintControl {

    protected val statusOnDark = Build.VERSION.SDK_INT < Build.VERSION_CODES.M

    protected fun getToolbarColor(context: Context, @Theme theme: Int, @Color color: Int): Int {
        return context.getAppThemeColor(theme, color, needDark = false)
    }

    protected fun getStatusBarColor(context: Context, @Theme theme: Int, @Color color: Int): Int {
        return context.getAppThemeColor(theme, color, statusOnDark)
    }

}