package sgtmelon.scriptum.app.screen.callback

/**
 * Интерфейс для общения [TextNoteViewModel] с [TextNoteFragment]
 */
interface TextNoteMenuCallback {

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

    fun onMenuBind()

    fun onMenuConvert()

    fun onMenuDelete()

    /**
     * [mode] - Установка режима редактирования
     */
    fun onMenuEdit(mode: Boolean)

}