package sgtmelon.scriptum.infrastructure.develop

import androidx.recyclerview.widget.DiffUtil

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