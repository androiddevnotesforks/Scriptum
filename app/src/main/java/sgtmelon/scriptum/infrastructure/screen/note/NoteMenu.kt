package sgtmelon.scriptum.infrastructure.screen.note

import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteViewModelImpl
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteViewModelImpl

/**
 * Interface for communication dataBinding with [TextNoteViewModelImpl]/[RollNoteViewModelImpl].
 */
interface NoteMenu {

    //region Inside bin

    fun onMenuRestore()

    fun onMenuRestoreOpen()

    fun onMenuClear()

    //endregion

    //region Edit mode

    fun onMenuUndo()

    fun onMenuRedo()

    fun onMenuRank()

    fun onMenuColor()

    /**
     * Return true on success save
     * [changeMode] - need change mode or not.
     */
    fun onMenuSave(changeMode: Boolean): Boolean

    //endregion

    //region Read mode

    fun onMenuNotification()

    fun onMenuBind()

    fun onMenuConvert()

    fun onMenuDelete()

    fun onMenuEdit()

    //endregion

}