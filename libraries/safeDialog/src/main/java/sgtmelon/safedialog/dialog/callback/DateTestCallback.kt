package sgtmelon.safedialog.dialog.callback

import sgtmelon.safedialog.dialog.time.DateDialog
import java.util.Calendar

/**
 * Interface for manipulate [DateDialog]. Call it only from UI tests.
 */
interface DateTestCallback {
    fun onTestUpdateDate(calendar: Calendar)
}