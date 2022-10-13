package sgtmelon.scriptum.infrastructure.develop.adapter.holder.entity

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.ParentHolder
import sgtmelon.scriptum.databinding.ItemPrintVisibleBinding
import sgtmelon.scriptum.infrastructure.develop.PrintItem

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