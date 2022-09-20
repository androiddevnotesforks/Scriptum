package sgtmelon.scriptum.cleanup.extension

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

const val NO_ID_LAYOUT = -1

fun <T : ViewDataBinding> Activity.inflateBinding(@LayoutRes layoutId: Int): T? {
    if (layoutId == NO_ID_LAYOUT) return null

    return DataBindingUtil.setContentView(this, layoutId)
}

fun <T : ViewDataBinding> LayoutInflater.inflateBinding(
    @LayoutRes layoutId: Int,
    parent: ViewGroup?,
    attachToParent: Boolean = false
): T {
    return DataBindingUtil.inflate(this, layoutId, parent, attachToParent)
}

fun <T : ViewDataBinding> ViewGroup.inflateBinding(@LayoutRes layoutId: Int,
                                                   attachToParent: Boolean = false): T {
    val inflater = LayoutInflater.from(context)
    return DataBindingUtil.inflate(inflater, layoutId, this, attachToParent)
}

fun ViewGroup.inflateView(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}