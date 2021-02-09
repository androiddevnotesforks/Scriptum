package sgtmelon.scriptum.presentation.control.toolbar.tint

import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.extension.getAppTheme

/**
 * Control note toolbar holder color.
 */
class HolderTintControl(
    context: Context,
    private val window: Window,
    private val holder: View?
) : ParentTintControl(context),
        IHolderTintControl {

    @Theme private val theme: Int? = context.getAppTheme()

    override fun setupColor(@Color color: Int) {
        if (theme == null || theme == Theme.DARK) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = getStatusBarColor(theme, color)
        }

        holder?.setBackgroundColor(getToolbarColor(theme, color))
    }

    companion object {
        operator fun get(context: Context, window: Window, holder: View?): IHolderTintControl {
            return HolderTintControl(context, window, holder)
        }
    }
}