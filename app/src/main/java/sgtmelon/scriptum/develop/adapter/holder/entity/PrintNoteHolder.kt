package sgtmelon.scriptum.develop.adapter.holder.entity

import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemPrintNoteBinding
import sgtmelon.scriptum.develop.model.PrintItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder

class PrintNoteHolder(private val binding: ItemPrintNoteBinding) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Note) = with(binding) {
        with(item.entity) {
            idText.text = context.getString(R.string.print_db_id, id.toString())
            createText.text = context.getString(R.string.print_db_create, create)
            changeText.text = context.getString(R.string.print_db_change, change)
            colorText.text = context.getString(R.string.print_db_color, color.toString())
            typeText.text = context.getString(R.string.print_db_type, type.name)
            rankIdText.text = context.getString(R.string.print_db_rank_id, rankId.toString())
            rankPsText.text = context.getString(R.string.print_db_rank_position, rankPs.toString())
            binText.text = context.getString(R.string.print_db_bin, isBin.toString())
            statusText.text = context.getString(R.string.print_db_status, isStatus.toString())
            nameText.text = context.getString(R.string.print_db_name, name)
            noteText.text = context.getString(R.string.print_db_text, text)
        }
    }
}