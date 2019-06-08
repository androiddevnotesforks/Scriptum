package sgtmelon.scriptum.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Configuration
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.StringDef
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.annotation.Theme
import java.util.*

/**
 * ViewGroup элемент для создания ripple анимации
 *
 * @author SerjantArbuz
 */
class RippleContainer : RelativeLayout {

    private var isAnimate = false

    private val animatorList = ArrayList<Animator>()
    private val animatorSet = AnimatorSet()

    private val viewList = ArrayList<RippleView>()

    constructor (context: Context) : super(context)

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor (context: Context, attrs: AttributeSet, style: Int) : super(context, attrs, style)

    /**
     * Вызывать метод перед [startAnimation]
     * Элемент, относительно которого будет расчитываться центр для ripple, передавать через [hookView]
     */
    fun setupAnimation(@Theme theme: Int, @ColorInt fillColor: Int, hookView: View): RippleContainer {
        val paint = Paint().apply {
            isAntiAlias = true

            style = if (theme == Theme.light) Paint.Style.STROKE else Paint.Style.FILL
            strokeWidth = resources.getDimension(R.dimen.stroke_4dp)
            color = fillColor
        }

        val viewSize = hookView.width / 1.3
        val maxSize = Math.max(width, height)

        val count = maxSize / (viewSize / 2).toInt()
        val duration = 1000L * count / 2

        val scaleTo = (maxSize / viewSize).toFloat() * when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> if (theme == Theme.light) 2f else 1.5f
            else -> if (theme == Theme.light) 1.7f else 1.2f
        }

        val layoutParams = LayoutParams(viewSize.toInt(), viewSize.toInt()).apply {
            addRule(CENTER_HORIZONTAL, TRUE)
            topMargin = hookView.top + ((hookView.height - viewSize) / 2).toInt()
        }

        val simpleDelay = duration / count

        animatorList.apply {
            add(hookView.getAnimator(AnimName.SCALE_X, 0, simpleDelay, 1f, 0.95f, 1f))
            add(hookView.getAnimator(AnimName.SCALE_Y, 0, simpleDelay, 1f, 0.95f, 1f))
        }

        for (i in 0 until count) {
            val view = RippleView(context).apply { this.paint = paint }

            addView(view, layoutParams)
            viewList.add(view)

            val startDelay = i * simpleDelay

            animatorList.apply {
                add(view.getAnimator(AnimName.SCALE_X, startDelay, duration, SCALE_FROM, scaleTo))
                add(view.getAnimator(AnimName.SCALE_Y, startDelay, duration, SCALE_FROM, scaleTo))
                add(view.getAnimator(AnimName.ALPHA, startDelay, duration, ALPHA_FROM, ALPHA_TO))
            }
        }

        animatorSet.playTogether(animatorList)

        return this
    }

    fun startAnimation() {
        if (!isAnimate) {
            isAnimate = true

            viewList.forEach { it.visibility = View.VISIBLE }
            animatorSet.start()
        }
    }

    fun stopAnimation() {
        if (isAnimate) {
            isAnimate = false

            viewList.forEach { it.visibility = View.INVISIBLE }
            animatorSet.end()
        }
    }

    private companion object {

        const val SCALE_FROM = 1f
        const val ALPHA_FROM = 0.7f
        const val ALPHA_TO = 0f

        fun View.getAnimator(@AnimName animName: String, startDelay: Long, duration: Long,
                             vararg values: Float): ObjectAnimator =
                ObjectAnimator.ofFloat(this, animName, *values).apply {
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.RESTART

                    interpolator = when (animName) {
                        AnimName.ALPHA -> DecelerateInterpolator()
                        else -> AccelerateDecelerateInterpolator()
                    }

                    this.startDelay = startDelay
                    this.duration = duration
                }

        @StringDef(AnimName.SCALE_X, AnimName.SCALE_Y, AnimName.ALPHA)
        annotation class AnimName {
            companion object {
                const val SCALE_X = "ScaleX"
                const val SCALE_Y = "ScaleY"
                const val ALPHA = "Alpha"
            }
        }

    }

}