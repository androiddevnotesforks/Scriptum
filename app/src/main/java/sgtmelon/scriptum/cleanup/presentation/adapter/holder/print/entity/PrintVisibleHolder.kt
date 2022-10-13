package sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity

import android.view.View
import android.widget.TextView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.ParentHolder
import sgtmelon.scriptum.infrastructure.develop.PrintAdapter
import sgtmelon.scriptum.infrastructure.develop.PrintItem

/**
 * Holder for developer screens and work inside [PrintAdapter].
 */
class PrintVisibleHolder(itemView: View) : ParentHolder(itemView) {

    // TODO add view binding

    private val idText = itemView.findViewById<TextView>(R.id.print_visible_id_text)
    private val noteIdText = itemView.findViewById<TextView>(R.id.print_visible_note_id_text)
    private val valueText = itemView.findViewById<TextView>(R.id.print_visible_value_text)

    fun bind(item: PrintItem.Visible) {
        val context = itemView.context

        with(item.entity) {
            idText.text = context.getString(R.string.print_db_id, id.toString())
            noteIdText.text = context.getString(R.string.print_db_note_id, noteId.toString())
            valueText.text = context.getString(R.string.print_db_value, value.toString())
        }
    }
}