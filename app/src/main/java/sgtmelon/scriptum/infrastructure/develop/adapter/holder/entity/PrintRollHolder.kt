package sgtmelon.scriptum.infrastructure.develop.adapter.holder.entity

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.ParentHolder
import sgtmelon.scriptum.databinding.ItemPrintRollBinding
import sgtmelon.scriptum.infrastructure.develop.PrintItem

class PrintRollHolder(private val binding: ItemPrintRollBinding) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Roll) = with(binding) {
        with(item.entity) {
            idText.text = context.getString(R.string.print_db_id, id.toString())
            noteIdText.text = context.getString(R.string.print_db_note_id, noteId.toString())
            positionText.text = context.getString(R.string.print_db_position, position.toString())
            checkText.text = context.getString(R.string.print_db_check, isCheck.toString())
            rollText.text = context.getString(R.string.print_db_text, text)
        }
    }
}