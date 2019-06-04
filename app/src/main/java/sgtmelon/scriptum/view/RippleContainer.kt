package sgtmelon.scriptum.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RelativeLayout
import sgtmelon.scriptum.R
import java.util.*

class RippleContainer(context: Context) : RelativeLayout(context) {

    private var isAnimate = false

    private val animatorSet = AnimatorSet().apply {
        interpolator = AccelerateDecelerateInterpolator()
    }

    private val animatorList = ArrayList<Animator>()
    private val rippleViewList = ArrayList<RippleView>()

    init {
        setupAnimation()
    }

    private fun setupAnimation() {
        if (isInEditMode) return

        val rippleColor = Color.parseColor("#0099CC")

        val rippleDurationTime = 3000
        val rippleAmount = 6
        val rippleScale = 6.0f
        val rippleRadius = resources.getDimension(R.dimen.icon_48dp)

        val rippleDelay = rippleDurationTime / rippleAmount

        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = rippleColor
        }

        val rippleParams = LayoutParams((2 * rippleRadius).toInt(), (2 * rippleRadius).toInt())
        rippleParams.addRule(CENTER_IN_PARENT, TRUE)

        for (i in 0 until rippleAmount) {
            val rippleView = RippleView(context).apply { this.paint = paint }

            addView(rippleView, rippleParams)
            rippleViewList.add(rippleView)

            val scaleXAnim = ObjectAnimator.ofFloat(rippleView, "ScaleX", 1f, rippleScale).apply {
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                startDelay = (i * rippleDelay).toLong()
                duration = rippleDurationTime.toLong()
            }

            animatorList.add(scaleXAnim)

            val scaleYAnim = ObjectAnimator.ofFloat(rippleView, "ScaleY", 1f, rippleScale).apply {
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                startDelay = (i * rippleDelay).toLong()
                duration = rippleDurationTime.toLong()
            }

            animatorList.add(scaleYAnim)

            val alphaAnim = ObjectAnimator.ofFloat(rippleView, "Alpha", 1f, 0f).apply {
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                startDelay = (i * rippleDelay).toLong()
                duration = rippleDurationTime.toLong()
            }

            animatorList.add(alphaAnim)
        }

        animatorSet.playTogether(animatorList)
    }

    fun startRippleAnimation() {
        if (!isAnimate) {
            for (rippleView in rippleViewList) {
                rippleView.visibility = View.VISIBLE
            }
            animatorSet.start()
            isAnimate = true
        }
    }

    fun stopRippleAnimation() {
        if (isAnimate) {
            animatorSet.end()
            isAnimate = false
        }
    }

}