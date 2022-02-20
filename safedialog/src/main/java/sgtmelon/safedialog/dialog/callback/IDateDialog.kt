package sgtmelon.safedialog.dialog.callback

import sgtmelon.safedialog.dialog.time.DateDialog
import java.util.*

/**
 * Interface for manipulate [DateDialog]. Call it only from UI tests.
 */
interface IDateDialog {
    fun updateDate(calendar: Calendar)
}