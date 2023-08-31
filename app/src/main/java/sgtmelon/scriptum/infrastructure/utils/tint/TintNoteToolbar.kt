package sgtmelon.scriptum.infrastructure.utils.tint

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.Window
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.extension.getDisplayedTheme
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.utils.ColorTransformation
import sgtmelon.test.idling.getWaitIdling

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

    private val barColor = ColorTransformation()
    private val indicatorColor = ColorTransformation()

    /**
     * Variable for save previous color, and use it during animation preparation
     * (set [ColorTransformation.from]).
     */
    private var previousColor: Color = startColor

    init {
        val theme = theme
        if (theme != null) {
            setupColorAnimator(theme)
            setupColor(theme, getColors(theme, startColor))
        }
    }

    private fun setupColorAnimator(theme: ThemeDisplayed) {
        colorAnimator.duration = context.resources.getInteger(R.integer.color_transition_time)
            .toLong()

        colorAnimator.removeAllUpdateListeners()
        colorAnimator.addUpdateListener {
            val ratio = it.animatedFraction
            setupColor(theme, Colors(barColor[ratio], indicatorColor[ratio]))
        }
    }

    private fun setupColor(theme: ThemeDisplayed, colors: Colors) = when (theme) {
        ThemeDisplayed.LIGHT -> {
            window?.statusBarColor = colors.bar
            toolbar?.setBackgroundColor(colors.bar)
        }
        ThemeDisplayed.DARK -> {
            indicator?.setBackgroundColor(colors.indicator)
        }
    }

    fun startTint(colorTo: Color) {
        val theme = theme ?: return

        setupAnimationFrom(theme, previousColor)
        setupAnimationTo(theme, colorTo)
        previousColor = colorTo

        if (barColor.isReady() || indicatorColor.isReady()) {
            colorAnimator.start()
            getWaitIdling().start(colorAnimator.duration)
        }
    }

    private fun setupAnimationFrom(theme: ThemeDisplayed, color: Color) {
        with(getColors(theme, color)) {
            barColor.from = bar
            indicatorColor.from = indicator
        }
    }

    private fun setupAnimationTo(theme: ThemeDisplayed, color: Color) {
        with(getColors(theme, color)) {
            barColor.to = bar
            indicatorColor.to = indicator
        }
    }
}