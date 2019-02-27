package sgtmelon.scriptum.app.screen.note.roll

/**
 * Интерфейс для общения [RollNoteViewModel] с [RollNoteFragment]
 */
interface MenuCallback {

    /**
     * Внутри корзины
     */

    fun onMenuRestore()

    fun onMenuRestoreOpen()

    fun onMenuClear()

    /**
     * Режим редактирования
     */

    fun onMenuUndo()

    fun onMenuRedo()

    fun onMenuRank()

    fun onMenuColor()

    /**
     * При успешном сохранении возвращает - true
     * [changeMode] - Надо ли менять режим редактирования,
     */
    fun onMenuSave(changeMode: Boolean): Boolean

    /**
     * Режим просмотра
     */

    fun onMenuCheck()

    fun onMenuBind()

    fun onMenuConvert()

    fun onMenuDelete()

    /**
     * [mode] - Установка режима редактирования
     */
    fun onMenuEdit(mode: Boolean)

}