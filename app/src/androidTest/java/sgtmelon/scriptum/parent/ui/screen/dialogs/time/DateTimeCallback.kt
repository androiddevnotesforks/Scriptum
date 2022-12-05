package sgtmelon.scriptum.parent.ui.screen.dialogs.time

import java.util.Calendar

/**
 * Interface for catch results of [DateDialogUi] and [TimeDialogUi].
 */
interface DateTimeCallback {

    fun dateResetResult()

    fun timeSetResult(calendar: Calendar)
}