package sgtmelon.scriptum.presentation.adapter.holder.print

import android.view.View
import android.widget.TextView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.presentation.adapter.PrintAdapter
import sgtmelon.scriptum.presentation.adapter.holder.ParentHolder

/**
 * Holder for developer screens and work inside [PrintAdapter].
 */
class PrintVisibleHolder(itemView: View) : ParentHolder(itemView) {

    private val idText = itemView.findViewById<TextView>(R.id.print_visible_id_text)
    private val noteIdText = itemView.findViewById<TextView>(R.id.print_visible_note_id_text)
    private val valueText = itemView.findViewById<TextView>(R.id.print_visible_value_text)

    fun bind(item: PrintItem.Visible) {
        val context = itemView.context
        val entity = item.entity

        idText.text = context.getString(R.string.print_id, entity.id.toString())
        noteIdText.text = context.getString(R.string.print_note_id, entity.noteId.toString())
        valueText.text = context.getString(R.string.print_value, entity.value.toString())
    }
}