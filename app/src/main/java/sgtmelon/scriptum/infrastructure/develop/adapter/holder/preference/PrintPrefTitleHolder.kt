package sgtmelon.scriptum.infrastructure.develop.adapter.holder.preference

import sgtmelon.scriptum.databinding.ItemPrintPrefTitleBinding
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.develop.PrintItem

class PrintPrefTitleHolder(
    private val binding: ItemPrintPrefTitleBinding
) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Preference.Title) {
        binding.titleText.text = itemView.context.getString(item.title)
    }
}