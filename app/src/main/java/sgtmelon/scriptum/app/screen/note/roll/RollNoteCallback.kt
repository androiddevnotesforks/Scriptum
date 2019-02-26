package sgtmelon.scriptum.app.screen.note.roll

import sgtmelon.scriptum.app.control.input.InputCallback
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.office.state.NoteState

/**
 * Интерфейс для общения [RollNoteViewModel] с [RollNoteFragment]
 */
interface RollNoteCallback {

    /**
     * Установка элементов для биндинга, которые постоянные
     */
    fun setupBinding(rankEmpty: Boolean)

    fun setupToolbar(@ColorDef color: Int, noteState: NoteState)

    fun setupDialog(rankNameArray: Array<String> )

    fun setupRecycler()

    fun setupEnter(inputCallback: InputCallback)

    /**
     *
     */

    fun bindEdit(mode: Boolean, noteItem: NoteItem)

    fun bindEnter()

    fun bindInput(isUndoAccess: Boolean, isRedoAccess: Boolean, isSaveEnable: Boolean)

    fun tintToolbar(@ColorDef color: Int)

    fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean)

    fun clearEnter(): String

    fun scrollToItem(simpleClick: Boolean, p: Int, list: MutableList<RollItem>)

    fun updateNoteState(noteState: NoteState)

    fun notifyItemRemoved(p: Int, list: MutableList<RollItem>)

    fun notifyItemMoved(from: Int, to: Int, list: MutableList<RollItem>)

    fun showRankDialog(rankCheck: BooleanArray)

    fun showColorDialog(color: Int)

    fun showConvertDialog()

}