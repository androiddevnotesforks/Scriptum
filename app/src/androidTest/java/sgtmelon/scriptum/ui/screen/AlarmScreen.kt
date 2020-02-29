package sgtmelon.scriptum.ui.screen

import sgtmelon.extension.getText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.sheet.RepeatSheetDialogUi
import sgtmelon.scriptum.ui.item.NoteItemUi
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen
import java.util.*

/**
 * Class for UI control of [AlarmActivity].
 */
class AlarmScreen(
        private val noteItem: NoteItem,
        private val dateList: List<String>?
) : ParentRecyclerScreen(R.id.alarm_recycler), IPressBack {

    private val repeatArray = context.resources.getIntArray(R.array.pref_alarm_repeat_array)

    //region Views

    private val parentContainer = getViewById(R.id.alarm_parent_container)
    private val rippleContainer = getViewById(R.id.alarm_ripple_container)
    private val logoView = getViewById(R.id.alarm_logo_view)
    private val buttonContainer = getViewById(R.id.alarm_button_container)

    private val disableButton = getViewById(R.id.alarm_disable_button)
    private val repeatButton = getViewById(R.id.alarm_repeat_button)
    private val moreButton = getViewById(R.id.alarm_more_button)

    private fun getItem() = NoteItemUi(recyclerView, p = 0)

    //endregion

    fun openTextNote(isRankEmpty: Boolean = true, func: TextNoteScreen.() -> Unit = {}) {
        recyclerView.click(p = 0)
        TextNoteScreen(func, State.READ, noteItem, isRankEmpty)
    }

    fun openRollNote(isRankEmpty: Boolean = true, func: RollNoteScreen.() -> Unit = {}) {
        recyclerView.click(p = 0)
        RollNoteScreen(func, State.READ, noteItem, isRankEmpty)
    }

    fun onClickDisable() {
        disableButton.click()
    }

    fun onClickRepeat() {
        repeatButton.click()
        onRepeat()
    }

    fun openMoreDialog(func: RepeatSheetDialogUi.() -> Unit = {}) = apply {
        moreButton.click()
        RepeatSheetDialogUi(func)
    }

    fun waitRepeat() = waitBefore(AlarmViewModel.CANCEL_DELAY) { onRepeat() }

    private fun onRepeat() {
        val calendar = getTime(min = repeatArray[repeat])

        while (dateList?.contains(calendar.getText()) == true) {
            calendar.add(Calendar.MINUTE, 1)
        }

        noteItem.alarmDate = calendar.getText()
    }


    fun onAssertItem(noteItem: NoteItem) {
        getItem().assert(noteItem)
    }

    /**
     * TODO #TEST assert color
     */
    fun assert() {
        parentContainer.isDisplayed()
        rippleContainer.isDisplayed()

        logoView.isDisplayed()
                .withSize(R.dimen.icon_128dp, R.dimen.icon_128dp)
                .withDrawableColor(R.mipmap.img_logo)

        recyclerView.isDisplayed()

        buttonContainer.isDisplayed()

        disableButton.isDisplayed().withText(R.string.button_disable, R.attr.clAccent)
        repeatButton.isDisplayed().withText(R.string.button_repeat, R.attr.clAccent)
        moreButton.isDisplayed()
                .withDrawableAttr(R.drawable.ic_more, R.attr.clAccent)
                .withContentDescription(R.string.description_button_alarm_more)
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