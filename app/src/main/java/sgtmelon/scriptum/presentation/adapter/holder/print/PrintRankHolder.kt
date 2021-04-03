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
class PrintRankHolder(itemView: View) : ParentHolder(itemView) {

    private val idText = itemView.findViewById<TextView>(R.id.print_rank_id_text)
    private val positionText = itemView.findViewById<TextView>(R.id.print_rank_position_text)
    private val visibleText = itemView.findViewById<TextView>(R.id.print_rank_visible_text)
    private val nameText = itemView.findViewById<TextView>(R.id.print_rank_name_text)
    private val noteIdText = itemView.findViewById<TextView>(R.id.print_rank_note_id_text)

    fun bind(item: PrintItem.Rank) {
        val context = itemView.context
        val entity = item.entity

        idText.text = context.getString(R.string.print_id, entity.id.toString())
        positionText.text = context.getString(R.string.print_position, entity.position.toString())
        visibleText.text = context.getString(R.string.print_visible, entity.isVisible.toString())
        nameText.text = context.getString(R.string.print_name, entity.name)
        noteIdText.text = context.getString(R.string.print_note_id, entity.noteId.joinToString())
    }
}