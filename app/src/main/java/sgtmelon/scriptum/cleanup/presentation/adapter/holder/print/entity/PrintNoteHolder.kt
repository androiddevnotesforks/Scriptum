package sgtmelon.scriptum.cleanup.presentation.adapter.holder.print.entity

import android.view.View
import android.widget.TextView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.presentation.adapter.PrintAdapter
import sgtmelon.scriptum.cleanup.presentation.adapter.holder.ParentHolder

/**
 * Holder for developer screens and work inside [PrintAdapter].
 */
class PrintNoteHolder(itemView: View) : ParentHolder(itemView) {

    private val idText = itemView.findViewById<TextView>(R.id.print_note_id_text)
    private val createText = itemView.findViewById<TextView>(R.id.print_note_create_text)
    private val changeText = itemView.findViewById<TextView>(R.id.print_note_change_text)
    private val colorText = itemView.findViewById<TextView>(R.id.print_note_color_text)
    private val typeText = itemView.findViewById<TextView>(R.id.print_note_type_text)
    private val rankIdText = itemView.findViewById<TextView>(R.id.print_note_rank_id_text)
    private val rankPsText = itemView.findViewById<TextView>(R.id.print_note_rank_ps_text)
    private val binText = itemView.findViewById<TextView>(R.id.print_note_bin_text)
    private val statusText = itemView.findViewById<TextView>(R.id.print_note_status_text)
    private val nameText = itemView.findViewById<TextView>(R.id.print_note_name_text)
    private val textView = itemView.findViewById<TextView>(R.id.print_note_text)

    fun bind(item: PrintItem.Note) {
        val context = itemView.context

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
            textView.text = context.getString(R.string.print_db_text, text)
        }
    }
}