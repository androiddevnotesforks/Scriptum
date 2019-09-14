package sgtmelon.scriptum.screen.ui.callback.note.roll

import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel

/**
 * Interface for communication [RollNoteViewModel] with [RollNoteFragment]
 */
interface IRollNoteMenu {

    //region Внутри корзины

    fun onMenuRestore()

    fun onMenuRestoreOpen()

    fun onMenuClear()

    //endregion

    //region Режим редактирования

    fun onMenuUndo()

    fun onMenuRedo()

    fun onMenuRank()

    fun onMenuColor()

    /**
     * При успешном сохранении возвращает - true, [changeMode] - надо ли менять режим редактирования
     */
    fun onMenuSave(changeMode: Boolean): Boolean

    //endregion

    //region Режим просмотра

    fun onMenuNotification()

    fun onMenuBind()

    fun onMenuConvert()

    fun onMenuDelete()

    /**
     * @param editMode установка режима редактирования
     */
    fun onMenuEdit(editMode: Boolean)

    //endregion

}