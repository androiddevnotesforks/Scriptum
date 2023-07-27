package sgtmelon.scriptum.infrastructure.utils.extensions

import android.os.Build
import android.widget.EdgeEffect
import android.widget.ScrollView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.cleanup.extension.getNoteOverscrollColor
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

fun RecyclerView.disableChangeAnimations() {
    (itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
}

fun RecyclerView.setOverscrollColor(color: Color) {
    val overscrollColor = context.getNoteOverscrollColor(color)

    edgeEffectFactory = object : RecyclerView.EdgeEffectFactory() {
        override fun createEdgeEffect(view: RecyclerView, direction: Int): EdgeEffect {
            return EdgeEffect(view.context).apply { this.color = overscrollColor }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
fun ScrollView.setOverscrollColor(color: Color) {
    val overscrollColor = context.getNoteOverscrollColor(color)
    topEdgeEffectColor = overscrollColor
    bottomEdgeEffectColor = overscrollColor
}