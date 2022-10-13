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
class PrintRankHolder(itemView: View) : ParentHolder(itemView) {

    // TODO add view binding

    private val idText = itemView.findViewById<TextView>(R.id.print_rank_id_text)
    private val positionText = itemView.findViewById<TextView>(R.id.print_rank_position_text)
    private val visibleText = itemView.findViewById<TextView>(R.id.print_rank_visible_text)
    private val nameText = itemView.findViewById<TextView>(R.id.print_rank_name_text)
    private val noteIdText = itemView.findViewById<TextView>(R.id.print_rank_note_id_text)

    fun bind(item: PrintItem.Rank) {
        val context = itemView.context

        with(item.entity) {
            idText.text = context.getString(R.string.print_db_id, id.toString())
            positionText.text = context.getString(R.string.print_db_position, position.toString())
            visibleText.text = context.getString(R.string.print_db_visible, isVisible.toString())
            nameText.text = context.getString(R.string.print_db_name, name)
            noteIdText.text = context.getString(R.string.print_db_note_id, noteId.joinToString())
        }
    }
}