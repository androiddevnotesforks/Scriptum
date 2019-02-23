package sgtmelon.scriptum.app.screen.note.text

interface MenuCallback {

    fun onRestoreClick()

    fun onRestoreOpenClick()

    fun onClearClick()

    /**
     *
     */

    fun onUndoClick()

    fun onRedoClick()

    fun onRankClick()

    fun onColorClick()

    fun onSaveClick(changeMode: Boolean)

    /**
     *
     */

    fun onCheckClick()

    fun onBindClick()

    fun onConvertClick()

    fun onDeleteClick()

    fun onEditClick(mode: Boolean)

}