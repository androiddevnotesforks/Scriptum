package sgtmelon.scriptum.develop.adapter.holder.entity

import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemPrintVisibleBinding
import sgtmelon.scriptum.develop.model.PrintItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder

class PrintVisibleHolder(
    private val binding: ItemPrintVisibleBinding
) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Visible) = with(binding) {
        with(item.entity) {
            idText.text = context.getString(R.string.print_db_id, id.toString())
            noteIdText.text = context.getString(R.string.print_db_note_id, noteId.toString())
            valueText.text = context.getString(R.string.print_db_value, value.toString())
        }
    }
}