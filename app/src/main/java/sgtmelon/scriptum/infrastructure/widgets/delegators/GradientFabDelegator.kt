package sgtmelon.scriptum.infrastructure.widgets.delegators

import android.animation.Animator
import android.animation.AnimatorSet
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.animation.addListener
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.screen.ScriptumApplication
import sgtmelon.scriptum.infrastructure.utils.DelayedJob
import sgtmelon.scriptum.infrastructure.utils.extensions.getAlphaAnimator
import sgtmelon.scriptum.infrastructure.utils.extensions.getScaleXAnimator
import sgtmelon.scriptum.infrastructure.utils.extensions.getScaleYAnimator
import sgtmelon.scriptum.infrastructure.utils.extensions.makeGone
import sgtmelon.scriptum.infrastructure.utils.extensions.makeVisible

internal class GradientFabDelegator(
    private val activity: AppCompatActivity,
    private var isVisible: Boolean = true,
    private val onClick: (view: View) -> Unit
) : DefaultLifecycleObserver {

    private val overlayJob = DelayedJob(activity.lifecycle)

    private var parentCard: CardView? = null
    private var gradientView: View? = null
    private var clickView: View? = null

    private var gradientDrawable: AnimationDrawable? = null

    /** Instance of last run animator from [runChangeVisibility]. */
    private var lastAnimator: Animator? = null

    init {
        activity.lifecycle.addObserver(this)
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
        if (ScriptumApplication.skipAnimation) return

        if (isPlay) {
            gradientDrawable?.start()
        } else {
            gradientDrawable?.stop()
        }
    }

    /**
     * [withGap] needed for understanding: this will be repeatable call or single. If repeatable
     * when need skip some calls (which happen during [GAP_DURATION]).
     */
    fun changeVisibility(isVisible: Boolean, withGap: Boolean) {
        /** Prevent repeatable calls with same [isVisible] key. */
        if (this.isVisible == isVisible) return

        this.isVisible = isVisible

        if (withGap) {
            overlayJob.start(GAP_DURATION) { runChangeVisibility(isVisible) }
        } else {
            runChangeVisibility(isVisible)
        }
    }

    @MainThread
    private fun runChangeVisibility(isVisible: Boolean) {
        parentCard?.isEnabled = isVisible
        clickView?.isEnabled = isVisible

        /**
         * Need cancel previous animation before starting new, because otherwise it may cause
         * some lags related with animator listener.
         */
        lastAnimator?.removeAllListeners()
        lastAnimator?.cancel()
        lastAnimator = AnimatorSet().setupCardAnimator(isVisible)
        lastAnimator?.start()
    }

    private fun AnimatorSet.setupCardAnimator(isVisible: Boolean): AnimatorSet {
        val parentCard = parentCard ?: return this

        val duration = activity.resources.getInteger(R.integer.fab_change_time).toLong()
        val alpha = if (isVisible) 1f else 0f
        val scale = if (isVisible) 1f else 0.2f

        val alphaInterpolator = DecelerateInterpolator()
        val scaleInterpolator = AccelerateDecelerateInterpolator()

        setDuration(duration).addListener(
            onStart = { if (isVisible) parentCard.makeVisible() },
            onEnd = { if (!isVisible) parentCard.makeGone() }
        )

        playTogether(
            getAlphaAnimator(parentCard, alpha).apply { interpolator = alphaInterpolator },
            getScaleXAnimator(parentCard, scale).apply { interpolator = scaleInterpolator },
            getScaleYAnimator(parentCard, scale).apply { interpolator = scaleInterpolator }
        )

        return this
    }

    companion object {
        private const val GAP_DURATION = 75L
    }
}