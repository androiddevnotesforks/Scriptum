package sgtmelon.scriptum.cleanup.test.parent.situation

import sgtmelon.extensions.toCalendar
import sgtmelon.scriptum.parent.ui.screen.dialogs.time.DateDialogUi
import sgtmelon.scriptum.parent.ui.screen.dialogs.time.TimeDialogUi

/**
 * Interface describes [DateDialogUi] and [TimeDialogUi] tests.
 */
interface DateTimeDialogCase {

    fun dateReset()

    fun DateDialogUi.runDateReset() = reset()

    fun toastToday()

    fun DateDialogUi.runToastToday() = applyDate { set(addMin = 2).applyTime() }

    fun toastOther()

    /**
     * Min = 2 because may happen case when clock will be near 0.59 time and apply button will
     * be not enabled.
     */
    fun DateDialogUi.runToastOther() = set(addDay = 1).applyDate {
        set(addMin = 2).applyTime()
    }

    fun timeApplyEnablePast()

    fun DateDialogUi.runTimeApplyEnablePast() = applyDate { set(addMin = -1).set(addMin = 3) }

    fun timeApplyEnableList()

    fun DateDialogUi.runTimeApplyEnableList(alarmDate: String) {
        val calendar = alarmDate.toCalendar()
        val dateList = arrayListOf(alarmDate)

        set(calendar).applyDate(dateList) { set(calendar) }
    }
}