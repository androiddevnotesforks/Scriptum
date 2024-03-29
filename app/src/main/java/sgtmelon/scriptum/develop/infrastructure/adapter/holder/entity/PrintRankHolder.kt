package sgtmelon.scriptum.develop.infrastructure.adapter.holder.entity

import sgtmelon.scriptum.R
import sgtmelon.scriptum.databinding.ItemPrintRankBinding
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder

class PrintRankHolder(private val binding: ItemPrintRankBinding) : ParentHolder(binding.root) {

    fun bind(item: PrintItem.Rank) = with(binding) {
        with(item.entity) {
            idText.text = context.getString(R.string.print_db_id, id.toString())
            positionText.text = context.getString(R.string.print_db_position, position.toString())
            visibleText.text = context.getString(R.string.print_db_visible, isVisible.toString())
            nameText.text = context.getString(R.string.print_db_name, name)
            noteIdText.text = context.getString(R.string.print_db_note_id, noteId.joinToString())
        }
    }
}