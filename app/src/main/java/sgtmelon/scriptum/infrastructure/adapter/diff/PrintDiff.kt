package sgtmelon.scriptum.infrastructure.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem

/**
 * Diff for [PrintItem].
 */
class PrintDiff : DiffUtil.ItemCallback<PrintItem>() {

    override fun areItemsTheSame(oldItem: PrintItem, newItem: PrintItem): Boolean {
        return oldItem.type == newItem.type && oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PrintItem, newItem: PrintItem): Boolean {
        return oldItem == newItem
    }
}