package sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint

import android.content.Context
import android.view.View
import android.view.Window
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.extension.geDisplayedTheme
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed

/**
 * Control note toolbar holder color.
 */
class HolderTintControl(
    context: Context,
    private val window: Window,
    private val holder: View?
) : ParentTintControl(context),
        IHolderTintControl {

    private val theme: ThemeDisplayed? = context.geDisplayedTheme()

    override fun setupColor(@Color color: Int) {
        if (theme == null || theme == ThemeDisplayed.DARK) return

        window.statusBarColor = getStatusBarColor(theme, color)

        holder?.setBackgroundColor(getToolbarColor(theme, color))
    }

    companion object {
        operator fun get(context: Context, window: Window, holder: View?): IHolderTintControl {
            return HolderTintControl(context, window, holder)
        }
    }
}