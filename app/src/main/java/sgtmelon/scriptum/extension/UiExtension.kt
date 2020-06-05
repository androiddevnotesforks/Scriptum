package sgtmelon.scriptum.extension

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R

fun Activity.beforeFinish(func: () -> Unit) {
    func()
    finish()
}


fun View.showKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(this, 0)
}

fun Activity.hideKeyboard() {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}


fun Context.showToast(@StringRes stringId: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(stringId), length).show()
}

fun Context.showToast(text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, length).show()
}


fun ViewGroup.createVisibleAnim(target: View?, isVisible: Boolean,
                                @IntegerRes durationId: Int = R.integer.info_fade_time) = let {
    val visibility = if (isVisible) View.VISIBLE else View.GONE

    if (target == null || target.visibility == visibility) return@let

    val time = context.resources.getInteger(durationId)
    val transition = Fade().setDuration(time.toLong()).addTarget(target)

    TransitionManager.beginDelayedTransition(it, transition)

    target.visibility = visibility
}

/**
 * Extension for fast check permission Granted/Denied.
 */
fun Int.isGranted() = this == PackageManager.PERMISSION_GRANTED
fun Int.notGranted() = !isGranted()