package sgtmelon.scriptum.cleanup.presentation.control.toolbar.tint

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.Window
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.extension.getAppSimpleColor
import sgtmelon.scriptum.cleanup.extension.getDisplayedTheme
import sgtmelon.scriptum.cleanup.extension.getNoteToolbarColor
import sgtmelon.scriptum.infrastructure.model.key.ColorShade
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.utils.ColorTransformation

/**
 * Control note toolbar tint.
 */
class TintNoteToolbarDelegator(
    context: Context,
    private val window: Window,
    private val toolbar: View?,
    private val indicator: View?,
    startColor: Color
) : TintBarDelegator(context) {

    private val theme: ThemeDisplayed? = context.getDisplayedTheme()

    private val colorAnimator: ValueAnimator = ValueAnimator.ofFloat(0F, 1F)

    private val statusBarColor = ColorTransformation()
    private val toolbarColor = ColorTransformation()
    private val indicatorColor = ColorTransformation()

    init {
        setupColorAnimator()
        setupColor(startColor)
    }

    private fun setupColorAnimator() {
        if (theme == null) return

        val updateColorsListener = ValueAnimator.AnimatorUpdateListener {
            val ratio = it.animatedFraction

            if (theme != ThemeDisplayed.DARK) {
                window.statusBarColor = statusBarColor[ratio]
                toolbar?.setBackgroundColor(toolbarColor[ratio])
            } else {
                indicator?.setBackgroundColor(indicatorColor[ratio])
            }
        }

        colorAnimator.removeAllUpdateListeners()
        colorAnimator.addUpdateListener(updateColorsListener)
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

        statusBarColor.from = context.getNoteToolbarColor(theme, color, needDark = false)
        toolbarColor.from = context.getNoteToolbarColor(theme, color, needDark = false)
        indicatorColor.from = context.getAppSimpleColor(color, ColorShade.DARK)
    }

    /**
     * Set end [color] and start animation if need.
     */
    fun startTint(color: Color) {
        if (theme == null) return

        statusBarColor.to = context.getNoteToolbarColor(theme, color, needDark = false)
        toolbarColor.to = context.getNoteToolbarColor(theme, color, needDark = false)
        indicatorColor.to = context.getAppSimpleColor(color, ColorShade.DARK)

        if (statusBarColor.isReady()
            || toolbarColor.isReady()
            || indicatorColor.isReady()) {
            colorAnimator.start()
        }
    }
}