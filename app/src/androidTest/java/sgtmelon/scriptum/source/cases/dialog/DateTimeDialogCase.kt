package sgtmelon.scriptum.source.cases.dialog

import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.source.ui.screen.dialogs.time.DateDialogUi
import sgtmelon.scriptum.source.ui.screen.dialogs.time.TimeDialogUi
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest

/**
 * Parent class for tests of [DateDialogUi] and [TimeDialogUi].
 */
abstract class DateTimeDialogCase<T : NoteItem> : ParentUiRotationTest(),
    DialogCloseCase,
    DialogRotateCase {

    /** Insert note WITHOUT alarm notification. */
    abstract fun insert(): T

    /** Insert note WITH alarm notification. */
    abstract fun insertNotification(): T

    /** Implementation must open [DateDialogUi] for future manipulations. */
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
        val alarmDate = insertNotification().alarm.date
        val calendar = alarmDate.toCalendar()

        set(calendar).applyDate(listOf(alarmDate)) { set(calendar) }
    }

    override fun rotateWork() = launchDateDialog(insert()) {
        rotate.switch()
        assert()
        applyDate {
            rotate.switch()
            assert()
        }
    }

    open fun rotateReset() = launchDateDialog(insertNotification()) {
        rotate.switch()
        assert()
    }
}