package sgtmelon.scriptum.cleanup.extension

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.IntegerRes
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.test.idling.addIdlingListener

fun ViewGroup.createVisibleAnim(
    target: View?,
    isVisible: Boolean,
    @IntegerRes durationId: Int = R.integer.info_fade_time
) = let {
    val visibility = if (isVisible) View.VISIBLE else View.GONE

    if (target == null || target.visibility == visibility) return@let

    val time = context.resources.getInteger(durationId)
    val transition = Fade().setDuration(time.toLong()).addTarget(target).addIdlingListener()

    TransitionManager.beginDelayedTransition(it, transition)

    target.visibility = visibility
}

inline fun RecyclerView.setFirstRunAnimation(
    isFirstRun: Boolean,
    @AnimRes id: Int,
    supportsChangeAnimations: Boolean = true,
    crossinline onFinish: () -> Unit
) {
    if (isFirstRun) {
        layoutAnimation = AnimationUtils.loadLayoutAnimation(context, id)
        layoutAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationStart(anim: Animation?) = Unit
            override fun onAnimationRepeat(anim: Animation?) = Unit
            override fun onAnimationEnd(anim: Animation?) {
                setDefaultAnimator(supportsChangeAnimations, onFinish)
            }
        }
    } else {
        setDefaultAnimator(supportsChangeAnimations, onFinish)
    }
}

inline fun RecyclerView.setDefaultAnimator(
    supportsChangeAnimations: Boolean = true,
    crossinline onFinish: () -> Unit
) {
    itemAnimator = object : DefaultItemAnimator() {
        override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) = onFinish()
    }.apply {
        this.supportsChangeAnimations = supportsChangeAnimations
    }
}