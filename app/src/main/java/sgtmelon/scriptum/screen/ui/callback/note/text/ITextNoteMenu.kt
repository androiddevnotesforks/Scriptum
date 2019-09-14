package sgtmelon.scriptum.screen.ui.callback.note.text

import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteFragment]
 */
interface ITextNoteMenu {

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