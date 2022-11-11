package sgtmelon.scriptum.cleanup.ui.screen

import java.util.Calendar
import sgtmelon.extensions.getCalendar
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.exception.NoteCastException
import sgtmelon.scriptum.cleanup.basic.extension.waitBefore
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.getAppSimpleColor
import sgtmelon.scriptum.cleanup.ui.ParentRecyclerScreen
import sgtmelon.scriptum.cleanup.ui.item.NoteItemUi
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.infrastructure.widgets.ripple.RippleConverter
import sgtmelon.scriptum.parent.ui.dialogs.sheet.RepeatSheetDialogUi
import sgtmelon.scriptum.parent.ui.feature.BackPress
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withContentDescription
import sgtmelon.test.cappuccino.utils.withDrawableAttr
import sgtmelon.test.cappuccino.utils.withDrawableColor
import sgtmelon.test.cappuccino.utils.withSize
import sgtmelon.test.cappuccino.utils.withTag
import sgtmelon.test.cappuccino.utils.withText

/**
 * Class for UI control of [AlarmActivity].
 */
class AlarmScreen(
    private val item: NoteItem,
    private val dateList: List<String>?
) : ParentRecyclerScreen(R.id.recycler_view), BackPress {

    private val converter = RippleConverter()

    private val repeatArray = context.resources.getIntArray(R.array.pref_alarm_repeat_array)

    //region Views

    private val parentContainer = getViewById(R.id.parent_container)
    private val rippleContainer = getViewById(R.id.ripple_container)
    private val logoView = getViewById(R.id.logo_view)
    private val buttonContainer = getViewById(R.id.button_container)

    private val disableButton = getViewById(R.id.disable_button)
    private val repeatButton = getViewById(R.id.repeat_button)
    private val moreButton = getViewById(R.id.more_button)

    private fun getItem() = NoteItemUi(recyclerView, p = 0)

    //endregion

    fun openTextNote(isRankEmpty: Boolean = true, func: TextNoteScreen.() -> Unit = {}) {
        if (item !is NoteItem.Text) throw NoteCastException()

        recyclerView.click(p = 0)
        TextNoteScreen(func, NoteState.READ, item, isRankEmpty)
    }

    fun openRollNote(isRankEmpty: Boolean = true, func: RollNoteScreen.() -> Unit = {}) {
        if (item !is NoteItem.Roll) throw NoteCastException()

        recyclerView.click(p = 0)
        RollNoteScreen(func, NoteState.READ, item, isRankEmpty)
    }

    fun onClickDisable() {
        disableButton.click()
    }

    fun onClickRepeat(): Calendar {
        /**
         * If click happen in corner seconds value (like 0.59) and calendar will be receiver in
         * another minute (like 1.10) this may lead false tests.
         */
        while (getCalendar().get(Calendar.SECOND) > 50) {
            waitBefore(time = 5000)
        }

        repeatButton.click()
        return onRepeat()
    }

    fun openMoreDialog(func: RepeatSheetDialogUi.() -> Unit = {}) = apply {
        moreButton.click()
        RepeatSheetDialogUi(func)
    }

    fun waitRepeat() = waitBefore(AlarmActivity.TIMEOUT_TIME) { onRepeat() }

    private fun onRepeat(): Calendar {
        val calendar = getClearCalendar(addMinutes = repeatArray[preferencesRepo.repeat.ordinal])

        while (dateList?.contains(calendar.toText()) == true) {
            calendar.add(Calendar.MINUTE, 1)
        }

        item.alarmDate = calendar.toText()

        return calendar
    }


    fun onAssertItem(item: NoteItem) = getItem().assert(item)

    fun assert() = apply {
        parentContainer.isDisplayed()

        val fillColor = context.getAppSimpleColor(item.color, converter.getRippleShade(appTheme))
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
        inline operator fun invoke(
            func: AlarmScreen.() -> Unit,
            item: NoteItem,
            dateList: List<String>? = null
        ): AlarmScreen {
            return AlarmScreen(item, dateList)
                .assert()
                .apply(func)
        }
    }
}