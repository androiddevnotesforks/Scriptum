package sgtmelon.scriptum.app.screen.note.roll

/**
 * Интерфейс для общения [RollNoteViewModel] с [RollNoteFragment]
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

    fun onSaveClick(changeMode: Boolean)

    /**
     * Режим просмотра
     */

    fun onCheckClick()

    fun onBindClick()

    fun onConvertClick()

    fun onDeleteClick()

    fun onEditClick(mode: Boolean)

}