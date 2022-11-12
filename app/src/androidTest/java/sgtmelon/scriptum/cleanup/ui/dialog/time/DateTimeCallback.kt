package sgtmelon.scriptum.cleanup.ui.dialog.time

import java.util.Calendar

/**
 * Interface for work results of [DateDialogUi] and [TimeDialogUi].
 */
interface DateTimeCallback {

    fun dateResetResult()

    fun timeSetResult(calendar: Calendar)
}