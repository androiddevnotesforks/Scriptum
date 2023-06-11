package sgtmelon.scriptum.cleanup.ui.screen.note

import sgtmelon.scriptum.source.ui.screen.dialogs.message.ConvertDialogUi

/**
 * Interface for [TextNoteScreen]/[RollNoteScreen].
 *
 * [T] - is screen which will displayed after convert.
 */
interface INoteAfterConvert<T> {

    /** Call this func after [ConvertDialogUi.positive]. */
    fun afterConvert(func: T.() -> Unit = {})

}