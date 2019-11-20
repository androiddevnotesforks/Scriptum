package sgtmelon.scriptum.ui.screen

import sgtmelon.extension.getString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.getTime
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen
import java.util.*

/**
 * Class for UI control of [AlarmActivity].
 */
class AlarmScreen(
        private val noteItem: NoteItem,
        private val dateList: List<String>?
) : ParentUi(), IPressBack {

    private val repeatArray = context.resources.getIntArray(R.array.value_alarm_repeat_array)

    //region Views

    private val parentContainer = getViewById(R.id.alarm_parent_container)
    private val rippleContainer = getViewById(R.id.alarm_ripple_container)
    private val logoView = getViewById(R.id.alarm_logo_view)
    private val recyclerView = getViewById(R.id.alarm_recycler)
    private val buttonContainer = getViewById(R.id.alarm_button_container)

    private val disableButton = getViewById(R.id.alarm_disable_button)
    private val postponeButton = getViewById(R.id.alarm_postpone_button)

    //endregion

    fun openTextNote(func: TextNoteScreen.() -> Unit = {}) {
        recyclerView.click(p = 0)
        TextNoteScreen.invoke(func, State.READ, noteItem)
    }

    fun openRollNote(func: RollNoteScreen.() -> Unit = {}) {
        recyclerView.click(p = 0)
        RollNoteScreen.invoke(func, State.READ, noteItem)
    }

    fun onClickDisable() {
        disableButton.click()
    }

    fun onClickPostpone() {
        postponeButton.click()
        onPostpone()
    }

    fun waitPostpone() = waitBefore(AlarmViewModel.CANCEL_DELAY) { onPostpone() }

    private fun onPostpone() {
        val calendar = getTime(min = repeatArray[repeat])

        while (dateList?.contains(calendar.getString()) == true) {
            calendar.add(Calendar.MINUTE, 1)
        }

        noteItem.alarmDate = calendar.getString()
    }

    fun assert() {
        parentContainer.isDisplayed()
        rippleContainer.isDisplayed()
        logoView.isDisplayed()
        recyclerView.isDisplayed()

        buttonContainer.isDisplayed()
        disableButton.isDisplayed()
        postponeButton.isDisplayed()
    }

    companion object {
        operator fun invoke(func: AlarmScreen.() -> Unit, noteItem: NoteItem,
                            dateList: List<String>? = null): AlarmScreen {
            return AlarmScreen(noteItem, dateList)
                    .apply { waitBefore(time = 500) { assert() } }
                    .apply(func)
        }
    }

}