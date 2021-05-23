package sgtmelon.scriptum.test

import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.ui.dialog.time.DateDialogUi
import sgtmelon.scriptum.ui.dialog.time.TimeDialogUi

/**
 * Interface describes [DateDialogUi] and [TimeDialogUi] tests.
 */
interface IDateTimeDialogTest {

    fun dateReset()

    fun DateDialogUi.runDateReset() = onClickReset()

    fun toastToday()

    fun DateDialogUi.runToastToday() = onClickApply { onTime(min = 2).onClickApply() }

    fun toastOther()

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