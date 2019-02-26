package sgtmelon.scriptum.app.screen.note.text

import sgtmelon.scriptum.app.control.input.InputIntf
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.office.state.NoteState

/**
 * Интерфейс для общения [TextNoteViewModel] с [TextNoteFragment]
 */
interface TextNoteCallback {

    /**
     * Установка элементов для биндинга, которые постоянные
     */
    fun setupBinding(noteItem: NoteItem, rankEmpty: Boolean)

    fun bindEdit(mode: Boolean, noteItem: NoteItem)

    fun bindInput(isUndoAccess: Boolean, isRedoAccess: Boolean)

    fun setupToolbar(@ColorDef color: Int, noteState: NoteState)

    fun setupDialog(rankNameArray: Array<String> )

    fun setupEnter(inputIntf: InputIntf)

    /**
     *
     */

    fun tintToolbar(@ColorDef color: Int)

    fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean)

    /**
     *
     */

    fun showRankDialog(rankCheck: BooleanArray)

    fun showColorDialog(color: Int)

    fun showConvertDialog()

}