package sgtmelon.scriptum.infrastructure.widgets.ripple

import android.animation.Animator
import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import sgtmelon.scriptum.cleanup.extension.geDisplayedTheme
import sgtmelon.scriptum.cleanup.extension.getAppSimpleColor
import sgtmelon.scriptum.infrastructure.model.key.Color

/**
 * ViewGroup element for create ripple animation.
 */
class RippleContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private var isAnimate = false

    /** Prevent calling any animation functions before [setupAnimation]. */
    private var isConfigure = false

    private val animatorList = ArrayList<Animator>()
    private val animatorSet = AnimatorSet()

    private val viewList = ArrayList<RippleView>()

    /**
     * Call this func before [startAnimation].
     *
     * [color] - should be used for create ripple.
     * [hookView] - element, which center (x/y) will be start position for [RippleView].
     */
    fun setupAnimation(color: Color, hookView: View) = apply {
        if (isConfigure) return@apply

        val converter = RippleConverter()
        val theme = context.geDisplayedTheme() ?: return@apply
        val paintStyle = converter.getPaintStyle(theme)
        val rippleShade = converter.getRippleShade(theme)
        val fillColor = context.getAppSimpleColor(color, rippleShade)

        /** This needed for UI testing: assert final ripple color. */
        tag = fillColor

        val settings = RippleSettings(theme, hookView, parentView = this)
        val animatorProvider = RippleAnimatorProvider(settings)

        animatorList.addAll(animatorProvider.getLogoList(hookView))

        for (i in 0 until settings.viewCount) {
            val view = RippleView(context).setup(paintStyle, fillColor)
            addView(view, settings.childParams)
            viewList.add(view)
            animatorList.addAll(animatorProvider.getItemList(view, i))
        }

        animatorSet.playTogether(animatorList)

        isConfigure = true
    }

    fun startAnimation() {
        if (!isConfigure || isAnimate) return

        for (it in viewList) {
            it.visibility = View.VISIBLE
        }

        isAnimate = true
        animatorSet.start()
    }

    fun stopAnimation() {
        if (!isConfigure || !isAnimate) return

        for (it in viewList) {
            it.visibility = View.INVISIBLE
        }

        isAnimate = false
        animatorSet.end()
        animatorList.clear()
    }
}