package sgtmelon.scriptum.cleanup.extension

import androidx.recyclerview.widget.RecyclerView

inline fun RecyclerView.ViewHolder.checkNoPosition(func: (Int) -> Unit) {
    val p = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
    func(p)
}