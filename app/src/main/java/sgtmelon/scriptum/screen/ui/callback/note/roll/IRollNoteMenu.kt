package sgtmelon.scriptum.screen.ui.callback.note.roll

import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel

/**
 * Interface for communication [RollNoteViewModel] with [RollNoteFragment]
 */
interface IRollNoteMenu {

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
     * [changeMode] - need change mode or not
     */
    fun onMenuSave(changeMode: Boolean): Boolean

    //endregion

    //region Read mode

    fun onMenuNotification()

    fun onMenuBind()

    fun onMenuConvert()

    fun onMenuDelete()

    /**
     * [isEdit] - setup edit mode
     */
    fun onMenuEdit(isEdit: Boolean)

    //endregion

}