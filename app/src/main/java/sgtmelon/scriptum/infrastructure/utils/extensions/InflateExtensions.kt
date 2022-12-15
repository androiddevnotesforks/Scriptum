package sgtmelon.scriptum.infrastructure.utils.extensions

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

const val NO_LAYOUT = -1

val Context.inflater: LayoutInflater get() = LayoutInflater.from(this)

fun <T : ViewDataBinding> Activity.inflateBinding(@LayoutRes layoutId: Int): T? {
    if (layoutId == NO_LAYOUT) return null

    return DataBindingUtil.setContentView(this, layoutId)
}

fun <T : ViewDataBinding> LayoutInflater.inflateBinding(
    @LayoutRes layoutId: Int,
    parent: ViewGroup?,
    attachToParent: Boolean = false
): T {
    return DataBindingUtil.inflate(this, layoutId, parent, attachToParent)
}

fun <T : ViewDataBinding> ViewGroup.inflateBinding(
    @LayoutRes layoutId: Int,
    attachToParent: Boolean = false
): T {
    return DataBindingUtil.inflate(context.inflater, layoutId, this, attachToParent)
}

fun ViewGroup.inflateView(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return context.inflater.inflate(layout, this, attachToRoot)
}