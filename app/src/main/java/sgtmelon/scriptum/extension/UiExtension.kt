package sgtmelon.scriptum.extension

import android.app.Activity
import android.content.pm.PackageManager
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.IntegerRes
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R

fun Activity.beforeFinish(func: () -> Unit) {
    func()
    finish()
}


fun View.showKeyboard() {
    context.getInputService()?.showSoftInput(this, 0)
}

fun Activity.hideKeyboard() {
    getInputService()?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}


/**
 * Function for detect when layout completely configure.
 */
fun ViewGroup.afterLayoutConfiguration(func: () -> Unit) {
    viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver?.removeOnGlobalLayoutListener(this)
            func()
        }
    })
}

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

/**
 * Extension for fast check permission Granted/Denied.
 */
fun Int.isGranted() = this == PackageManager.PERMISSION_GRANTED
fun Int.notGranted() = !isGranted()

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