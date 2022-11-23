package sgtmelon.scriptum.ui.cases

import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.ui.dialog.time.DateDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.time.TimeDialogUi
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Parent class for tests of [DateDialogUi] and [TimeDialogUi].
 */
abstract class DateTimeDialogCase<T : NoteItem> : ParentUiTest(),
    DialogCloseCase {

    abstract fun insert(): T

    abstract fun insertNotification(): T

    abstract fun launchDateDialog(item: T, func: DateDialogUi.() -> Unit)

    open fun dateReset() = launchDateDialog(insertNotification()) { reset() }

    /**
     * I think sometimes test will fail because will be time = 23:59 o'clock, and after
     * adding 2 minutes it will be another day. So be careful.
     */
    open fun toastToday() = launchDateDialog(insert()) { applyDate { set(addMin = 2).applyTime() } }

    /**
     * Min = 2 because may happen case when clock will be near 0.59 time and apply button will
     * be not enabled.
     */
    open fun toastOther() = launchDateDialog(insert()) {
        set(addDay = 1).applyDate { set(addMin = 2).applyTime() }
    }

    open fun timeApplyEnablePast() = launchDateDialog(insert()) {
        applyDate { set(addMin = -1).set(addMin = 3) }
    }

    open fun timeApplyEnableList() = launchDateDialog(insert()) {
        val alarmDate = insertNotification().alarmDate
        val calendar = alarmDate.toCalendar()

        set(calendar).applyDate(listOf(alarmDate)) { set(calendar) }
    }

}