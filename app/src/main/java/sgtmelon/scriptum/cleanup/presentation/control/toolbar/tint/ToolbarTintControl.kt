package sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.Window
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.state.MenuColorState
import sgtmelon.scriptum.cleanup.extension.getAppSimpleColor
import sgtmelon.scriptum.cleanup.extension.getDisplayedTheme
import sgtmelon.scriptum.cleanup.extension.getNoteToolbarColor
import sgtmelon.scriptum.infrastructure.model.key.ColorShade
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Control note toolbar tint.
 */
class ToolbarTintControl(
    context: Context,
    private val window: Window,
    private val toolbar: View?,
    private val indicator: View?,
    startColor: Color
) : ParentTintControl(context) {

    private val theme: ThemeDisplayed? = context.getDisplayedTheme()

    private val colorAnimator: ValueAnimator = ValueAnimator.ofFloat(0F, 1F)

    private val statusState = MenuColorState()
    private val toolbarState = MenuColorState()
    private val indicatorState = MenuColorState()

    init {
        setupColorAnimator()
        setupColor(startColor)
    }

    private fun setupColorAnimator() {
        if (theme == null) return

        val updateListener = ValueAnimator.AnimatorUpdateListener {
            val ratio = it.animatedFraction
            var blended: Int

            if (theme != ThemeDisplayed.DARK) {
                blended = statusState.blend(ratio)
                window.statusBarColor = blended
            }

            blended = toolbarState.blend(ratio)
            if (theme != ThemeDisplayed.DARK) toolbar?.setBackgroundColor(blended)

            blended = indicatorState.blend(ratio)
            if (theme == ThemeDisplayed.DARK) indicator?.setBackgroundColor(blended)
        }

        colorAnimator.removeAllUpdateListeners()
        colorAnimator.addUpdateListener(updateListener)
        colorAnimator.duration = context.resources.getInteger(R.integer.color_transition_time)
            .toLong()
    }

    private fun setupColor(color: Color) {
        if (theme == null) return

        if (theme != ThemeDisplayed.DARK) {
            window.statusBarColor = getStatusBarColor(theme, color)

            toolbar?.setBackgroundColor(getToolbarColor(theme, color))
        }

        indicator?.setBackgroundColor(context.getNoteToolbarColor(theme, color, needDark = true))

        setColorFrom(color)
    }


    fun setColorFrom(color: Color) = apply {
        if (theme == null) return@apply

        statusState.from = context.getNoteToolbarColor(theme, color, needDark = false)
        toolbarState.from = context.getNoteToolbarColor(theme, color, needDark = false)
        indicatorState.from = context.getAppSimpleColor(color, ColorShade.DARK)
    }

    /**
     * Set end [color] and start animation if need.
     */
    fun startTint(color: Color) {
        if (theme == null) return

        statusState.to = context.getNoteToolbarColor(theme, color, needDark = false)
        toolbarState.to = context.getNoteToolbarColor(theme, color, needDark = false)
        indicatorState.to = context.getAppSimpleColor(color, ColorShade.DARK)

        if (statusState.isDifferent()
            || toolbarState.isDifferent()
            || indicatorState.isDifferent()) {
            colorAnimator.start()
        }
    }
}