package sgtmelon.scriptum.presentation.control.toolbar.tint

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.Window
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.key.ColorShade
import sgtmelon.scriptum.domain.model.state.MenuColorState
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.getAppTheme
import sgtmelon.scriptum.extension.getNoteToolbarColor

/**
 * Control note toolbar tint.
 */
class ToolbarTintControl(
    context: Context,
    private val window: Window,
    private val toolbar: View?,
    private val indicator: View?,
    @Color startColor: Int
) : ParentTintControl(context),
    IToolbarTintControl {

    @Theme private val theme: Int? = context.getAppTheme()

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

            if (theme != Theme.DARK) {
                blended = statusState.blend(ratio)
                window.statusBarColor = blended
            }

            blended = toolbarState.blend(ratio)
            if (theme != Theme.DARK) toolbar?.setBackgroundColor(blended)

            blended = indicatorState.blend(ratio)
            if (theme == Theme.DARK) indicator?.setBackgroundColor(blended)
        }

        colorAnimator.removeAllUpdateListeners()
        colorAnimator.addUpdateListener(updateListener)
        colorAnimator.duration = context.resources.getInteger(R.integer.color_transition_time)
            .toLong()
    }

    private fun setupColor(@Color color: Int) {
        if (theme == null) return

        if (theme != Theme.DARK) {
            window.statusBarColor = getStatusBarColor(theme, color)

            toolbar?.setBackgroundColor(getToolbarColor(theme, color))
        }

        indicator?.setBackgroundColor(context.getNoteToolbarColor(theme, color, needDark = true))

        setColorFrom(color)
    }


    override fun setColorFrom(@Color color: Int) = apply {
        if (theme == null) return@apply

        statusState.from = context.getNoteToolbarColor(theme, color, statusOnDark)
        toolbarState.from = context.getNoteToolbarColor(theme, color, needDark = false)
        indicatorState.from = context.getAppSimpleColor(color, ColorShade.DARK)
    }

    override fun startTint(@Color color: Int) {
        if (theme == null) return

        statusState.to = context.getNoteToolbarColor(theme, color, statusOnDark)
        toolbarState.to = context.getNoteToolbarColor(theme, color, needDark = false)
        indicatorState.to = context.getAppSimpleColor(color, ColorShade.DARK)

        if (statusState.isDifferent()
                || toolbarState.isDifferent()
                || indicatorState.isDifferent()) colorAnimator.start()
    }

}