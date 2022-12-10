package sgtmelon.scriptum.develop.infrastructure.adapter.holder.preference

import sgtmelon.scriptum.databinding.ItemPrintPrefTitleBinding
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder

class PrintPrefTitleHolder(
    private val binding: ItemPrintPrefTitleBinding
) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Preference.Title) {
        binding.titleText.text = itemView.context.getString(item.title)
    }
}