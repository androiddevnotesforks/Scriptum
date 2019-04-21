package sgtmelon.scriptum.screen.callback.note.roll

import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel

/**
 * Интерфейс для общения [RollNoteViewModel] с [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
interface RollNoteMenuCallback {

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
     * @param changeMode надо ли менять режим редактирования,
     * @return При успешном сохранении возвращает - true
     */
    fun onMenuSave(changeMode: Boolean): Boolean

    //endregion

    //region Режим просмотра

    fun onMenuCheck()

    fun onMenuBind()

    fun onMenuConvert()

    fun onMenuDelete()

    /**
     * @param editMode установка режима редактирования
     */
    fun onMenuEdit(editMode: Boolean)

    //endregion

}