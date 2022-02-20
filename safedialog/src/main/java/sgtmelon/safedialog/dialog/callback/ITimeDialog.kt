package sgtmelon.safedialog.dialog.callback

import sgtmelon.safedialog.dialog.time.TimeDialog
import java.util.*

/**
 * Interface for manipulate [TimeDialog]. Call it only from UI tests.
 */
interface ITimeDialog {
    fun updateTime(calendar: Calendar)
}