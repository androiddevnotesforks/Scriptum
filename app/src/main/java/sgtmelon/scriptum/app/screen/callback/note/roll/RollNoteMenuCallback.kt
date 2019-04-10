package sgtmelon.scriptum.app.screen.callback.note.roll

import sgtmelon.scriptum.app.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.app.screen.vm.note.RollNoteViewModel

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

    fun onMenuLongUndo()

    fun onMenuRedo()

    fun onMenuLongRedo()

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
     * @param mode установка режима редактирования
     */
    fun onMenuEdit(mode: Boolean)

    //endregion

}