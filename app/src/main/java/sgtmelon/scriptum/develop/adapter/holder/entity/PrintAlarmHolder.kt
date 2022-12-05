package sgtmelon.scriptum.develop.adapter.holder.entity

import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemPrintAlarmBinding
import sgtmelon.scriptum.develop.model.PrintItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder

class PrintAlarmHolder(private val binding: ItemPrintAlarmBinding) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Alarm) = with(binding) {
        with(item.entity) {
            idText.text = context.getString(R.string.print_db_id, id.toString())
            noteIdText.text = context.getString(R.string.print_db_note_id, noteId.toString())
            dateText.text = context.getString(R.string.print_db_date, date)
        }
    }
}