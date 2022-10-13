package sgtmelon.scriptum.infrastructure.develop.adapter.holder.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.ParentHolder
import sgtmelon.scriptum.databinding.ItemPrintPrefKeyBinding
import sgtmelon.scriptum.infrastructure.develop.PrintItem

class PrintPrefKeyHolder(
    private val binding: ItemPrintPrefKeyBinding
) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Preference.Key) = with(binding) {
        keyText.text = context.getString(R.string.print_pref_key, item.key)
        defText.text = context.getString(R.string.print_pref_def, item.def)
        valueText.text = context.getString(R.string.print_pref_value, item.value)
    }
}