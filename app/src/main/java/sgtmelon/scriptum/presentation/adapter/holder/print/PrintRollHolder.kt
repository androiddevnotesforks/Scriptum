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
class PrintRollHolder(itemView: View) : ParentHolder(itemView) {

    private val idText = itemView.findViewById<TextView>(R.id.print_roll_id_text)
    private val noteIdText = itemView.findViewById<TextView>(R.id.print_roll_note_id_text)
    private val positionText = itemView.findViewById<TextView>(R.id.print_roll_position_text)
    private val checkText = itemView.findViewById<TextView>(R.id.print_roll_check_text)
    private val textView = itemView.findViewById<TextView>(R.id.print_roll_text)

    fun bind(item: PrintItem.Roll) {
        val context = itemView.context
        val entity = item.entity

        idText.text = context.getString(R.string.print_id, entity.id.toString())
        noteIdText.text = context.getString(R.string.print_note_id, entity.noteId.toString())
        positionText.text = context.getString(R.string.print_position, entity.position.toString())
        checkText.text = context.getString(R.string.print_check, entity.isCheck.toString())
        textView.text = context.getString(R.string.print_text, entity.text)
    }
}