package sgtmelon.scriptum.infrastructure.develop.adapter

import androidx.recyclerview.widget.DiffUtil
import sgtmelon.scriptum.infrastructure.develop.PrintItem

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