package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.time.DateDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.swipeLeft
import sgtmelon.scriptum.room.entity.AlarmEntity
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import java.util.*

/**
 * Class for UI control [DateDialog]
 */
class DateDialogUi(private val callback: INoteScreen, private val updateDate: Boolean) : ParentUi(),
        IDialogUi {

    //region Views

    private val dayPicker = getViewByName(name = "date_picker_day_picker")

    private val resetButton = getViewByText(R.string.dialog_button_reset)
    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

    //endregion

    private val currentDate get() = Calendar.getInstance()


    fun onClickAnotherDay() {
        repeat(times = 3) { dayPicker.swipeLeft() }
    }


    // TODO #TEST create UI

    fun onClickReset() = waitClose {
        resetButton.click()

        callback.noteModel.alarmEntity.apply {
            id = AlarmEntity.ND_ID
            date = AlarmEntity.ND_DATE
        }

        callback.fullAssert()
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply(func: TimeDialogUi.() -> Unit) = waitClose {
        applyButton.click()
        TimeDialogUi.invoke(func)
    }

    fun assert() {
        resetButton.isDisplayed(updateDate).isEnabled()

        cancelButton.isDisplayed().isEnabled()
        applyButton.isDisplayed().isEnabled()
    }

    companion object {
        operator fun invoke(func: DateDialogUi.() -> Unit, updateDate: Boolean,
                            callback: INoteScreen) =
                DateDialogUi(callback, updateDate).apply { waitOpen { assert() } }.apply(func)
    }

}