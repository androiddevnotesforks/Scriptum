package sgtmelon.scriptum.infrastructure.utils

import android.view.View

fun View.makeVisibleIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}