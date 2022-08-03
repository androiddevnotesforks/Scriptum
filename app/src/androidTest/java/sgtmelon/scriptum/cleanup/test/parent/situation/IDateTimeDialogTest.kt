package sgtmelon.scriptum.cleanup.test.parent.situation

import sgtmelon.common.utils.getCalendar
import sgtmelon.scriptum.cleanup.ui.dialog.time.DateDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.time.TimeDialogUi

/**
 * Interface describes [DateDialogUi] and [TimeDialogUi] tests.
 */
interface IDateTimeDialogTest {

    fun dateReset()

    fun DateDialogUi.runDateReset() = onClickReset()

    fun toastToday()

    fun DateDialogUi.runToastToday() = onClickApply { onTime(min = 2).onClickApply() }

    fun toastOther()

    /**
     * Min = 2 because may happen case when clock will be near 0.59 time and apply button wil
     * be not enabled.
     */
    fun DateDialogUi.runToastOther() = onDate(day = 1).onClickApply {
        onTime(min = 2).onClickApply()
    }

    fun timeApplyEnablePast()

    fun DateDialogUi.runTimeApplyEnablePast() = onClickApply { onTime(min = -1).onTime(min = 3) }

    fun timeApplyEnableList()

    fun DateDialogUi.runTimeApplyEnableList(alarmDate: String) {
        val calendar = alarmDate.getCalendar()
        val dateList = arrayListOf(alarmDate)

        onDate(calendar).onClickApply(dateList) { onTime(calendar) }
    }
}