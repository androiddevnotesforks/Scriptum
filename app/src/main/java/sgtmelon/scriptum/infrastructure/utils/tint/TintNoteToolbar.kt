package sgtmelon.scriptum.infrastructure.utils.tint

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
 * Control of note toolbar tinting.
 */
class TintNoteToolbar(
    context: Context,
    private val window: Window?,
    private val toolbar: View?,
    private val indicator: View?,
    startColor: Color
) : TintNoteBar(context) {

    private val theme: ThemeDisplayed? get() = context.getDisplayedTheme()

    private val colorAnimator: ValueAnimator = ValueAnimator.ofFloat(0F, 1F)

    private val statusBarColor = ColorTransformation()
    private val toolbarColor = ColorTransformation()
    private val indicatorColor = ColorTransformation()

    init {
        setupColorAnimator()
        setupColor(startColor)
    }

    private fun setupColorAnimator() {
        val theme = theme ?: return

        val updateColorsListener = ValueAnimator.AnimatorUpdateListener {
            val ratio = it.animatedFraction

            if (theme != ThemeDisplayed.DARK) {
                window?.statusBarColor = statusBarColor[ratio]
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
        val theme = theme ?: return

        if (theme != ThemeDisplayed.DARK) {
            window?.statusBarColor = getStatusBarColor(theme, color)
            toolbar?.setBackgroundColor(getToolbarColor(theme, color))
        } else {
            indicator?.setBackgroundColor(
                context.getNoteToolbarColor(theme, color, needDark = true)
            )
        }

        setColorFrom(color)
    }

    fun setColorFrom(color: Color) = apply {
        val theme = theme ?: return@apply

        statusBarColor.from = context.getNoteToolbarColor(theme, color, needDark = false)
        toolbarColor.from = context.getNoteToolbarColor(theme, color, needDark = false)
        indicatorColor.from = context.getAppSimpleColor(color, ColorShade.DARK)
    }

    /**
     * Set end [colorTo] and start animation if it needed.
     */
    fun startTint(colorTo: Color) {
        val theme = theme ?: return

        statusBarColor.to = context.getNoteToolbarColor(theme, colorTo, needDark = false)
        toolbarColor.to = context.getNoteToolbarColor(theme, colorTo, needDark = false)
        indicatorColor.to = context.getAppSimpleColor(colorTo, ColorShade.DARK)

        if (statusBarColor.isReady() || toolbarColor.isReady() || indicatorColor.isReady()) {
            colorAnimator.start()
        }
    }
}