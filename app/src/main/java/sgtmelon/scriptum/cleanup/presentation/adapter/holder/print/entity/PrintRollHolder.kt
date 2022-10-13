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
class PrintRollHolder(itemView: View) : ParentHolder(itemView) {

    // TODO add view binding

    private val idText = itemView.findViewById<TextView>(R.id.print_roll_id_text)
    private val noteIdText = itemView.findViewById<TextView>(R.id.print_roll_note_id_text)
    private val positionText = itemView.findViewById<TextView>(R.id.print_roll_position_text)
    private val checkText = itemView.findViewById<TextView>(R.id.print_roll_check_text)
    private val textView = itemView.findViewById<TextView>(R.id.print_roll_text)

    fun bind(item: PrintItem.Roll) {
        val context = itemView.context

        with(item.entity) {
            idText.text = context.getString(R.string.print_db_id, id.toString())
            noteIdText.text = context.getString(R.string.print_db_note_id, noteId.toString())
            positionText.text = context.getString(R.string.print_db_position, position.toString())
            checkText.text = context.getString(R.string.print_db_check, isCheck.toString())
            textView.text = context.getString(R.string.print_db_text, text)
        }
    }
}