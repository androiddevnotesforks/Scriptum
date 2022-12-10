package sgtmelon.scriptum.develop.infrastructure.adapter.holder.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemPrintPrefKeyBinding
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder

class PrintPrefKeyHolder(
    private val binding: ItemPrintPrefKeyBinding
) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Preference.Key) = with(binding) {
        keyText.text = context.getString(R.string.print_pref_key, item.key)
        defText.text = context.getString(R.string.print_pref_def, item.def)
        valueText.text = context.getString(R.string.print_pref_value, item.value)
    }
}