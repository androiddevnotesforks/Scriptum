package sgtmelon.scriptum.develop.adapter.holder.preference

import sgtmelon.scriptum.databinding.ItemPrintPrefPathBinding
import sgtmelon.scriptum.develop.model.PrintItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder

class PrintPrefPathHolder(
    private val binding: ItemPrintPrefPathBinding
) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Preference.Path) {
        binding.pathText.text = item.file.path
    }
}