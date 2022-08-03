package sgtmelon.scriptum.cleanup.ui.screen.note

import sgtmelon.scriptum.cleanup.ui.dialog.ConvertDialogUi

/**
 * Interface for [TextNoteScreen]/[RollNoteScreen].
 *
 * [T] - is screen which will displayed after convert.
 */
interface INoteAfterConvert<T> {

    /**
     * Call this func after [ConvertDialogUi.onClickYes].
     */
    fun afterConvert(func: T.() -> Unit = {})

}