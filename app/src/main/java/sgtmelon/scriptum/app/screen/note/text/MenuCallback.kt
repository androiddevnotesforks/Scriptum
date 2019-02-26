package sgtmelon.scriptum.app.screen.note.text

/**
 * Интерфейс для общения [TextNoteViewModel] с [TextNoteFragment]
 */
interface MenuCallback {

    /**
     * Внутри корзины
     */

    fun onRestoreClick()

    fun onRestoreOpenClick()

    fun onClearClick()

    /**
     * Режим редактирования
     */

    fun onUndoClick()

    fun onRedoClick()

    fun onRankClick()

    fun onColorClick()

    /**
     * При успешном сохранении возвращает - true
     * [changeMode] - Надо ли менять режим редактирования,
     */
    fun onSaveClick(changeMode: Boolean): Boolean

    /**
     * Режим просмотра
     */

    fun onBindClick()

    fun onConvertClick()

    fun onDeleteClick()

    /**
     * [mode] - Установка режима редактирования
     */
    fun onEditClick(mode: Boolean)

}