package sgtmelon.scriptum.infrastructure.develop.adapter.holder.entity

import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemPrintAlarmBinding
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.develop.PrintItem

class PrintAlarmHolder(private val binding: ItemPrintAlarmBinding) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Alarm) = with(binding) {
        with(item.entity) {
            idText.text = context.getString(R.string.print_db_id, id.toString())
            noteIdText.text = context.getString(R.string.print_db_note_id, noteId.toString())
            dateText.text = context.getString(R.string.print_db_date, date)
        }
    }
}