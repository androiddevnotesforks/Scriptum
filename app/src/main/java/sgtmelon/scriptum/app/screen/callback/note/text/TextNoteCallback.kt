package sgtmelon.scriptum.app.screen.callback.note.text

import sgtmelon.scriptum.app.control.input.InputCallback
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.state.NoteState
import sgtmelon.scriptum.app.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.app.screen.vm.note.TextNoteViewModel
import sgtmelon.scriptum.office.annot.def.ColorDef

/**
 * Интерфейс для общения [TextNoteViewModel] с [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
interface TextNoteCallback {

    /**
     * Установка элементов для биндинга, которые постоянные
     */
    fun setupBinding(rankEmpty: Boolean)

    fun setupToolbar(@ColorDef color: Int, noteState: NoteState)

    fun setupDialog(rankNameArray: List<String>)

    fun setupEnter(inputCallback: InputCallback)

    fun setupPanel()

    fun bindEdit(mode: Boolean, noteItem: NoteItem)

    fun bindInput(isUndoAccess: Boolean, isRedoAccess: Boolean)

    fun onPressBack(): Boolean

    fun tintToolbar(@ColorDef from: Int, @ColorDef to: Int)

    fun tintToolbar(@ColorDef color: Int)

    fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean)

    fun changeName(text: String, cursor: Int)

    fun changeText(text: String, cursor: Int)

    fun hideKeyboard()

    fun showRankDialog(rankCheck: BooleanArray)

    fun showColorDialog(color: Int)

    fun showConvertDialog()

}