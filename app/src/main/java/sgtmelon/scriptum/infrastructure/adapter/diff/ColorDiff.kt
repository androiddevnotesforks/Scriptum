package sgtmelon.scriptum.infrastructure.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Diff for [Color].
 */
class ColorDiff : DiffUtil.ItemCallback<Color>() {

    override fun areContentsTheSame(oldItem: Color, newItem: Color): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Color, newItem: Color): Boolean {
        return oldItem == newItem
    }
}