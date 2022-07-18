package sgtmelon.scriptum.ui.screen

import java.util.Calendar
import sgtmelon.common.utils.getCalendarWithAdd
import sgtmelon.common.utils.getNewCalendar
import sgtmelon.common.utils.getText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.exception.NoteCastException
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.basic.extension.withContentDescription
import sgtmelon.scriptum.basic.extension.withDrawableAttr
import sgtmelon.scriptum.basic.extension.withDrawableColor
import sgtmelon.scriptum.basic.extension.withSize
import sgtmelon.scriptum.basic.extension.withTag
import sgtmelon.scriptum.basic.extension.withText
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.key.ColorShade
import sgtmelon.scriptum.cleanup.extension.getAppSimpleColor
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.notification.AlarmViewModel
import sgtmelon.scriptum.cleanup.presentation.view.RippleContainer
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.dialog.sheet.RepeatSheetDialogUi
import sgtmelon.scriptum.ui.item.NoteItemUi
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [AlarmActivity].
 */
class AlarmScreen(
    private val item: NoteItem,
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
        if (item !is NoteItem.Text) throw NoteCastException()

        recyclerView.click(p = 0)
        TextNoteScreen(func, State.READ, item, isRankEmpty)
    }

    fun openRollNote(isRankEmpty: Boolean = true, func: RollNoteScreen.() -> Unit = {}) {
        if (item !is NoteItem.Roll) throw NoteCastException()

        recyclerView.click(p = 0)
        RollNoteScreen(func, State.READ, item, isRankEmpty)
    }

    fun onClickDisable() {
        disableButton.click()
    }

    fun onClickRepeat(): Calendar {
        /**
         * If click happen in corner seconds value (like 0.59) and calendar will be receiver in
         * another minute (like 1.10) this may lead false tests.
         */
        while (getNewCalendar().get(Calendar.SECOND) > 50) {
            waitBefore(time = 5000)
        }

        repeatButton.click()
        return onRepeat()
    }

    fun openMoreDialog(func: RepeatSheetDialogUi.() -> Unit = {}) = apply {
        moreButton.click()
        RepeatSheetDialogUi(func)
    }

    fun waitRepeat() = waitBefore(AlarmViewModel.CANCEL_DELAY) { onRepeat() }

    private fun onRepeat(): Calendar {
        val calendar = getCalendarWithAdd(min = repeatArray[preferences.repeat])

        while (dateList?.contains(calendar.getText()) == true) {
            calendar.add(Calendar.MINUTE, 1)
        }

        item.alarmDate = calendar.getText()

        return calendar
    }


    fun onAssertItem(item: NoteItem) = getItem().assert(item)

    fun assert() = apply {
        parentContainer.isDisplayed()

        val fillColor = context.getAppSimpleColor(item.color, getRippleShade(appTheme))
        rippleContainer.isDisplayed().withTag(fillColor)

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
        operator fun invoke(
            func: AlarmScreen.() -> Unit,
            item: NoteItem,
            dateList: List<String>? = null
        ): AlarmScreen {
            return AlarmScreen(item, dateList)
                .assert()
                .apply(func)
        }
    }

    /**
     * @Test - duplicate of original function in [RippleContainer].
     */
    // TODO add converter and apply it for RippleContainer
    private fun getRippleShade(theme: ThemeDisplayed): ColorShade {
        return when (theme) {
            ThemeDisplayed.LIGHT -> ColorShade.ACCENT
            ThemeDisplayed.DARK -> ColorShade.DARK
        }
    }
}