package sgtmelon.scriptum.screen.callback.note.roll

import sgtmelon.scriptum.control.input.InputCallback
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel

/**
 * Интерфейс для общения [RollNoteViewModel] с [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
interface RollNoteCallback {

    /**
     * Установка элементов для биндинга, которые постоянные
     */
    fun setupBinding(theme: Int, rankEmpty: Boolean)

    fun setupToolbar(theme: Int, @ColorDef color: Int, noteState: NoteState)

    fun setupDialog(rankNameList: List<String>)

    fun setupEnter(inputCallback: InputCallback)

    fun setupRecycler(inputCallback: InputCallback)

    fun bindEdit(editMode: Boolean, noteItem: NoteItem)

    fun bindNoteItem(noteItem: NoteItem)

    fun bindEnter()

    fun bindInput(inputAccess: InputControl.Access, isSaveEnabled: Boolean)

    fun bindItem(noteItem: NoteItem)

    fun onPressBack(): Boolean

    fun tintToolbar(@ColorDef from: Int, @ColorDef to: Int)

    fun tintToolbar(@ColorDef color: Int)

    fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean)

    fun focusOnEdit()

    fun changeName(text: String, cursor: Int)

    fun getEnterText(): String

    fun clearEnterText()

    fun scrollToItem(simpleClick: Boolean, p: Int, list: MutableList<RollItem>)

    fun changeCheckToggle(state: Boolean)

    fun updateNoteState(noteState: NoteState)

    fun notifyListItem(p: Int, item: RollItem)

    fun notifyList(list: MutableList<RollItem>)

    fun notifyDataSetChanged(list: MutableList<RollItem>)

    fun notifyItemInserted(p: Int, cursor: Int, list: MutableList<RollItem>)

    fun notifyItemChanged(p: Int, list: MutableList<RollItem>, cursor: Int)

    fun notifyItemRemoved(p: Int, list: MutableList<RollItem>)

    fun notifyItemMoved(from: Int, to: Int, list: MutableList<RollItem>)

    fun hideKeyboard()

    fun showRankDialog(rankCheck: BooleanArray)

    fun showColorDialog(color: Int)

    fun showConvertDialog()

}