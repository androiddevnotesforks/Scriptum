package sgtmelon.scriptum.control.toolbar

import android.animation.ValueAnimator
import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.Window
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.getAppThemeColor
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.model.state.MenuColorState
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment

/**
 * Control note toolbar tint.
 */
class ToolbarTintControl(
        private val context: Context,
        private val window: Window,
        private val toolbar: View?,
        private val indicator: View?,
        @Theme private val theme: Int,
        @Color startColor: Int
) : ParentTintControl(),
        IToolbarTintControl {

    private val colorAnimator: ValueAnimator = ValueAnimator.ofFloat(0F, 1F)

    private val statusState = MenuColorState()
    private val toolbarState = MenuColorState()
    private val indicatorState = MenuColorState()

    init {
        setupColorAnimator()
        setupColor(startColor)
    }

    private fun setupColorAnimator() {
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
        colorAnimator.duration = context.resources.getInteger(R.integer.color_transition_time).toLong()
    }

    private fun setupColor(@Color color: Int) {
        if (theme != Theme.DARK) {
            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = getStatusBarColor(context, theme, color)
            }

            toolbar?.setBackgroundColor(getToolbarColor(context, theme, color))
        }

        indicator?.setBackgroundColor(context.getAppThemeColor(theme, color, needDark = true))

        setColorFrom(color)
    }


    override fun setColorFrom(@Color color: Int) = apply {
        statusState.from = context.getAppThemeColor(theme, color, statusOnDark)
        toolbarState.from = context.getAppThemeColor(theme, color, needDark = false)
        indicatorState.from = context.getAppSimpleColor(color, ColorShade.DARK)
    }

    override fun startTint(@Color color: Int) {
        statusState.to = context.getAppThemeColor(theme, color, statusOnDark)
        toolbarState.to = context.getAppThemeColor(theme, color, needDark = false)
        indicatorState.to = context.getAppSimpleColor(color, ColorShade.DARK)

        if (statusState.isDifferent()
                || toolbarState.isDifferent()
                || indicatorState.isDifferent()) colorAnimator.start()
    }

}