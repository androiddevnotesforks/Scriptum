package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.time.DateDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class for UI control [DateDialog]
 */
class DateDialogUi(
        private val callback: INoteScreen,
        private val updateDate: Boolean
) : ParentUi(), IDialogUi {

    //region Views

    private val resetButton = getViewByText(R.string.dialog_button_reset)
    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    private val calendar = Calendar.getInstance()

    fun onChangeDate(day: Int) = apply {
        calendar.add(Calendar.DAY_OF_YEAR, day)

        DateDialog.callback?.updateDate(calendar)
    }

    fun onClickReset() {
        waitClose { resetButton.click() }

        callback.noteModel.alarmEntity.apply {
            date = AlarmEntity.ND_DATE
        }

        callback.fullAssert()
    }

    fun onClickCancel() {
        waitClose { cancelButton.click() }

        callback.fullAssert()
    }

    fun onClickApply(dateList: List<String> = ArrayList(),
                     func: TimeDialogUi.() -> Unit = {}) = waitClose {
        applyButton.click()
        TimeDialogUi.invoke(callback, calendar, dateList, func)
    }

    fun assert() {
        resetButton.isDisplayed(updateDate).isEnabled()

        cancelButton.isDisplayed().isEnabled()
        applyButton.isDisplayed().isEnabled()
    }

    companion object {
        operator fun invoke(updateDate: Boolean, callback: INoteScreen,
                            func: DateDialogUi.() -> Unit) =
                DateDialogUi(callback, updateDate).apply { waitOpen { assert() } }.apply(func)
    }

}