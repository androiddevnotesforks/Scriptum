package sgtmelon.scriptum.presentation.adapter.holder.print.entity

import android.view.View
import android.widget.TextView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.presentation.adapter.PrintAdapter
import sgtmelon.scriptum.presentation.adapter.holder.ParentHolder

/**
 * Holder for developer screens and work inside [PrintAdapter].
 */
class PrintAlarmHolder(itemView: View) : ParentHolder(itemView) {

    private val idText = itemView.findViewById<TextView>(R.id.print_alarm_id_text)
    private val noteIdText = itemView.findViewById<TextView>(R.id.print_alarm_note_id_text)
    private val dateText = itemView.findViewById<TextView>(R.id.print_alarm_date_text)

    fun bind(item: PrintItem.Alarm) {
        val context = itemView.context

        with(item.entity) {
            idText.text = context.getString(R.string.print_db_id, id.toString())
            noteIdText.text = context.getString(R.string.print_db_note_id, noteId.toString())
            dateText.text = context.getString(R.string.print_db_date, date)
        }
    }
}