package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note

import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.RollNoteViewModelImpl
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.note.TextNoteViewModelImpl

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