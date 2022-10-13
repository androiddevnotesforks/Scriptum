package sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference

import sgtmelon.scriptum.cleanup.presentation.adapter.holder.ParentHolder
import sgtmelon.scriptum.databinding.ItemPrintPrefTitleBinding
import sgtmelon.scriptum.infrastructure.develop.PrintItem

class PrintPrefTitleHolder(
    private val binding: ItemPrintPrefTitleBinding
) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Preference.Title) {
        binding.titleText.text = itemView.context.getString(item.title)
    }
}