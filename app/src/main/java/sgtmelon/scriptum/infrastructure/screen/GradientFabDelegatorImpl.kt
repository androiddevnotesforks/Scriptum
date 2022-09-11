package sgtmelon.scriptum.infrastructure.screen

import android.animation.AnimatorSet
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.extension.getAlphaAnimator
import sgtmelon.scriptum.cleanup.extension.getScaleXAnimator
import sgtmelon.scriptum.cleanup.extension.getScaleYAnimator

internal class GradientFabDelegatorImpl(
    private val activity: AppCompatActivity,
    private var isVisible: Boolean = true,
    private val onClick: (view: View) -> Unit
) : DefaultLifecycleObserver,
    GradientFabDelegator {

    private val overlayJob = DelayJobDelegator(ANIM_GAP)

    private var parentCard: CardView? = null
    private var gradientView: View? = null
    private var clickView: View? = null

    private var gradientDrawable: AnimationDrawable? = null

    private var lastVisibleState = isVisible

    init {
        activity.lifecycle.addObserver(overlayJob)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        setupViews()
        setupGradient()
    }

    private fun setupViews() {
        parentCard = activity.findViewById(R.id.gradient_fab_card)
        gradientView = activity.findViewById(R.id.gradient_fab_anim)
        clickView = activity.findViewById(R.id.gradient_fab_click)

        clickView?.setOnClickListener { onClick(it) }
    }

    private fun setupGradient() {
        val resources = activity.resources

        gradientDrawable = gradientView?.background as? AnimationDrawable
        gradientDrawable?.setEnterFadeDuration(resources.getInteger(R.integer.gradient_enter_time))
        gradientDrawable?.setExitFadeDuration(resources.getInteger(R.integer.gradient_exit_time))
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        parentCard = null
        gradientView = null
        clickView = null

        changePlay(isPlay = false)
        gradientDrawable = null
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        changePlay(isPlay = true)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        changePlay(isPlay = false)
    }

    private fun changePlay(isPlay: Boolean) {
        if (isPlay) {
            gradientDrawable?.start()
        } else {
            gradientDrawable?.stop()
        }
    }

    override fun changeVisibility(isVisible: Boolean, withGap: Boolean) {
        if (lastVisibleState == isVisible) return

        this.isVisible = isVisible
        this.lastVisibleState = isVisible

        if (withGap) {
            overlayJob.run { runChangeVisibility(isVisible) }
        } else {
            runChangeVisibility(isVisible)
        }
    }

    @MainThread
    private fun runChangeVisibility(isVisible: Boolean) {
        parentCard?.isEnabled = isVisible
        clickView?.isEnabled = isVisible
        startCardAnimation(isVisible)
    }

    private fun startCardAnimation(isVisible: Boolean) {
        val parentCard = parentCard ?: return

        val duration = activity.resources.getInteger(R.integer.fade_anim_time).toLong()

        val alpha = if (isVisible) 1f else 0f
        val scale = if (isVisible) 1f else 0.2f
        val alphaInterpolator = DecelerateInterpolator()
        val scaleInterpolator = AccelerateInterpolator()

        AnimatorSet().apply {
            this.duration = duration

            doOnStart { if (isVisible) parentCard.visibility = View.VISIBLE }
            doOnEnd { if (!isVisible) parentCard.visibility = View.GONE }

            playTogether(
                getAlphaAnimator(parentCard, alpha).apply { interpolator = alphaInterpolator },
                getScaleXAnimator(parentCard, scale).apply { interpolator = scaleInterpolator },
                getScaleYAnimator(parentCard, scale).apply { interpolator = scaleInterpolator }
            )
        }.start()
    }

    companion object {
        private const val ANIM_GAP = 100L
    }
}