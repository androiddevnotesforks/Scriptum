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
        val entity = item.entity

        idText.text = context.getString(R.string.print_id, entity.id.toString())
        createText.text = context.getString(R.string.print_create, entity.create)
        changeText.text = context.getString(R.string.print_change, entity.change)
        colorText.text = context.getString(R.string.print_color, entity.color.toString())
        typeText.text = context.getString(R.string.print_type, entity.type.name)
        rankIdText.text = context.getString(R.string.print_rank_id, entity.rankId.toString())
        rankPsText.text = context.getString(R.string.print_rank_position, entity.rankPs.toString())
        binText.text = context.getString(R.string.print_bin, entity.isBin.toString())
        statusText.text = context.getString(R.string.print_status, entity.isStatus.toString())
        nameText.text = context.getString(R.string.print_name, entity.name)
        textView.text = context.getString(R.string.print_text, entity.text)
    }
}