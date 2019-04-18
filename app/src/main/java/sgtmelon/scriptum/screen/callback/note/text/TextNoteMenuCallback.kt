package sgtmelon.scriptum.screen.callback.note.text

import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel

/**
 * Интерфейс для общения [TextNoteViewModel] с [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
interface TextNoteMenuCallback {

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

    fun onMenuBind()

    fun onMenuConvert()

    fun onMenuDelete()

    /**
     * @param mode установка режима редактирования
     */
    fun onMenuEdit(mode: Boolean)

    //endregion

}