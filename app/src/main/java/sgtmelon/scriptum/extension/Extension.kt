package sgtmelon.scriptum.extension

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.data.ReceiverData
import java.util.*

/**
 * Function for lazy property, call inside onCreate/onViewCreated.
 *
 * If lazy property not initialized and rotation happen it comes to troubles.
 * Value will be wrong.
 */
fun Any.initLazy() = this.run {}

fun <T : ViewDataBinding> Activity.inflateBinding(@LayoutRes layoutId: Int): T =
        DataBindingUtil.setContentView(this, layoutId)

fun <T : ViewDataBinding> LayoutInflater.inflateBinding(@LayoutRes layoutId: Int, parent: ViewGroup?,
                                                        attachToParent: Boolean = false): T =
        DataBindingUtil.inflate(this, layoutId, parent, attachToParent)

fun <T : ViewDataBinding> ViewGroup.inflateBinding(@LayoutRes layoutId: Int,
                                                   attachToParent: Boolean = false): T =
        DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, this, attachToParent)

fun ViewGroup.inflateView(@LayoutRes layout: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layout, this, attachToRoot)

fun Activity.beforeFinish(func: () -> Unit) {
    func()
    finish()
}

fun View.showKeyboard() {
    (context.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(this, 0)
}

fun Activity.hideKeyboard() {
    (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Context.showToast(@StringRes stringId: Int, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, getString(stringId), length).show()

fun Context.showToast(text: String, length: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, text, length).show()

fun Context.getDimen(value: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics).toInt()


fun RecyclerView.ViewHolder.checkNoPosition(func: () -> Unit): Boolean {
    if (adapterPosition == RecyclerView.NO_POSITION) return false

    func()
    return true
}

fun Context.sendTo(place: String, command: String, extras: Intent.() -> Unit = {}) =
        sendBroadcast(Intent(place).apply {
            putExtra(ReceiverData.Values.COMMAND, command)
            putExtras(Intent().apply(extras))
        })

fun ViewGroup.createVisibleAnim(target: View?, visible: Boolean,
                                @IntegerRes durationId: Int = R.integer.info_fade_time) = let {
    if (target == null) return

    val time = context.resources.getInteger(durationId)
    val transition = Fade().setDuration(time.toLong()).addTarget(target)

    TransitionManager.beginDelayedTransition(it, transition)

    target.visibility = if (visible) View.VISIBLE else View.GONE
}

fun String.toUri(): Uri? = let {
    return@let try {
        Uri.parse(it)
    } catch (t: Throwable) {
        null
    }
}

/**
 * Extension for fast check permission Granted/Denied
 */
fun Int.isGranted() = this == PackageManager.PERMISSION_GRANTED

/**
 * Make string upperCase without warning and without arguments
 */
fun String.toUpperCase() = toUpperCase(Locale.ROOT)

fun String.toLowerCase() = toLowerCase(Locale.ROOT)

@DimenRes fun Context.getDimenAttr(@AttrRes attr: Int): Int {
    val typedValue = TypedValue()

    theme.resolveAttribute(attr, typedValue, true)

    return typedValue.resourceId
}