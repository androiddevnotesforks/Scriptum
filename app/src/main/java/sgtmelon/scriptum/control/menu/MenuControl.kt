package sgtmelon.scriptum.control.menu

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.Window
import androidx.appcompat.widget.Toolbar
import sgtmelon.extension.getShortAnimTime
import sgtmelon.iconanim.IconCallback
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.getAppThemeColor
import sgtmelon.scriptum.extension.getTintDrawable
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.model.state.MenuColorState
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment

/**
 * Control menu of [TextNoteFragment], [RollNoteFragment] without animation
 *
 * Use only for API version < 21
 */
open class MenuControl(
        @Theme private val theme: Int,
        protected val context: Context,
        private val window: Window,
        protected val toolbar: Toolbar?,
        private val indicator: View?
) : IconCallback {

    // TODO add interface for communication

    private val statusOnDark = VERSION.SDK_INT < VERSION_CODES.M

    private val colorAnimator: ValueAnimator = ValueAnimator.ofFloat(0F, 1F)

    private val cancelOn: Drawable? = context.getTintDrawable(R.drawable.ic_cancel_enter)
    private val cancelOff: Drawable? = context.getTintDrawable(R.drawable.ic_cancel_exit)

    private val statusState = MenuColorState()
    private val toolbarState = MenuColorState()
    private val indicatorState = MenuColorState()

    init {
        val updateListener = ValueAnimator.AnimatorUpdateListener {
            val position = it.animatedFraction
            var blended: Int

            if (theme != Theme.DARK && VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                blended = statusState.blend(position)
                window.statusBarColor = blended
            }

            blended = toolbarState.blend(position)
            if (theme != Theme.DARK) toolbar?.setBackgroundColor(blended)

            blended = indicatorState.blend(position)
            if (theme == Theme.DARK) indicator?.setBackgroundColor(blended)
        }

        colorAnimator.addUpdateListener(updateListener)
        colorAnimator.duration = context.getShortAnimTime()
    }

    /**
     * Set colors for UI, [color] - startColor
     */
    fun setColor(@Color color: Int) = apply {
        if (theme != Theme.DARK) {
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = context.getAppThemeColor(theme, color, statusOnDark)
            }
            toolbar?.setBackgroundColor(context.getAppThemeColor(theme, color, needDark = false))
        }

        indicator?.setBackgroundColor(context.getAppThemeColor(theme, color, needDark = true))

        setColorFrom(color)
    }

    fun setColorFrom(@Color color: Int) = apply {
        statusState.from = context.getAppThemeColor(theme, color, statusOnDark)
        toolbarState.from = context.getAppThemeColor(theme, color, needDark = false)
        indicatorState.from = context.getAppSimpleColor(color, ColorShade.DARK)
    }

    /**
     * Set end [color] and start animation if need
     */
    fun startTint(@Color color: Int) {
        statusState.to = context.getAppThemeColor(theme, color, statusOnDark)
        toolbarState.to = context.getAppThemeColor(theme, color, needDark = false)
        indicatorState.to = context.getAppSimpleColor(color, ColorShade.DARK)

        if (statusState.isDifferent()
                || toolbarState.isDifferent()
                || indicatorState.isDifferent()) colorAnimator.start()
    }

    override fun setDrawable(drawableOn: Boolean, needAnim: Boolean) {
        toolbar?.navigationIcon = if (drawableOn) cancelOn else cancelOff
    }

    override fun setEnabled(enabled: Boolean) {}

}