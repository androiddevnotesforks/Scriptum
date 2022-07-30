package sgtmelon.scriptum.ui.dialog.time

import java.util.Calendar

/**
 * Interface for work results of [DateDialogUi] and [TimeDialogUi].
 */
interface DateTimeCallback {

    fun onDateDialogResetResult()

    fun onTimeDialogResult(calendar: Calendar)
}