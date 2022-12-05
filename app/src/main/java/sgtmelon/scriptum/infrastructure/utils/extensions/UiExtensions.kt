package sgtmelon.scriptum.infrastructure.utils.extensions

import android.app.Activity
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import sgtmelon.extensions.getInputService

inline fun Activity.beforeFinish(func: () -> Unit) {
    func()
    finish()
}

fun EditText.showKeyboard() {
    val flags = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
    context.getInputService().showSoftInput(this, flags)
}

fun Activity.hideKeyboard() {
    val flags = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
    getInputService().hideSoftInputFromWindow(currentFocus?.windowToken, flags)
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

/**
 * Function for detect when layout completely configure.
 */
inline fun ViewGroup.afterLayoutConfiguration(crossinline func: () -> Unit) {
    viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver?.removeOnGlobalLayoutListener(this)
            func()
        }
    })
}
