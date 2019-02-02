package sgtmelon.scriptum.app.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.NoteAdapter
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemNoteRollBinding
import sgtmelon.scriptum.databinding.ItemNoteTextBinding
import sgtmelon.scriptum.office.annot.def.TypeNoteDef

/**
 * Держатель заметки для [NoteAdapter]
 */
class NoteHolder : RecyclerView.ViewHolder {

    private val textBinding: ItemNoteTextBinding?
    private val rollBinding: ItemNoteRollBinding?

    val clickView: View

    /**
     * Конструктор для заметки - текстовой
     */
    constructor(bindingText: ItemNoteTextBinding) : super(bindingText.root) {
        this.textBinding = bindingText
        rollBinding = null

        clickView = itemView.findViewById(R.id.click_container)
    }

    /**
     * Конструкторо для заметки - списка
     */
    constructor(bindingRoll: ItemNoteRollBinding) : super(bindingRoll.root) {
        this.rollBinding = bindingRoll
        textBinding = null

        clickView = itemView.findViewById(R.id.click_container)
    }

    fun bind(noteItem: NoteItem, listRoll: List<RollItem>) {
        when (noteItem.type) {
            TypeNoteDef.text -> {
                textBinding!!.noteItem = noteItem

                textBinding.executePendingBindings()
            }
            TypeNoteDef.roll -> {
                rollBinding!!.noteItem = noteItem

                rollBinding.listRoll = listRoll
                rollBinding.executePendingBindings()
            }
        }
    }

}