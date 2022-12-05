package sgtmelon.scriptum.infrastructure.utils.extensions

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.disableChangeAnimations() {
    (itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
}

@Deprecated("don't depend of items animation")
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