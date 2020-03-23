package sgtmelon.scriptum.presentation.control.toolbar

import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Control note toolbar holder color.
 */
class HolderTintControl(
        private val context: Context,
        private val window: Window,
        private val holder: View?
) : ParentTintControl() {

    fun setupColor(@Theme theme: Int, @Color color: Int) {
        if (theme == Theme.DARK) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getStatusBarColor(context, theme, color)
        }

        holder?.setBackgroundColor(getToolbarColor(context, theme, color))
    }

}