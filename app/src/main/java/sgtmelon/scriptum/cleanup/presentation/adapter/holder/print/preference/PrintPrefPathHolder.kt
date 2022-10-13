package sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.preference

import sgtmelon.scriptum.cleanup.presentation.adapter.holder.ParentHolder
import sgtmelon.scriptum.databinding.ItemPrintPrefPathBinding
import sgtmelon.scriptum.infrastructure.develop.PrintItem

class PrintPrefPathHolder(
    private val binding: ItemPrintPrefPathBinding
) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Preference.Path) {
        binding.pathText.text = item.file.path
    }
}