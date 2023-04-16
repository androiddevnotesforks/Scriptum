package sgtmelon.scriptum.parent.ui.screen.alarm

import java.util.Calendar
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.getAppSimpleColor
import sgtmelon.scriptum.cleanup.ui.item.NoteItemUi
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.alarm.AlarmActivity
import sgtmelon.scriptum.infrastructure.widgets.ripple.RippleConverter
import sgtmelon.scriptum.parent.ui.feature.BackPress
import sgtmelon.scriptum.parent.ui.feature.OpenNote
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.parts.ContainerPart
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerPart
import sgtmelon.scriptum.parent.ui.screen.dialogs.sheet.RepeatSheetDialogUi
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.awaitMinuteEnd
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
) : ContainerPart(TestViewTag.ALARM),
    RecyclerPart<NoteItem, NoteItemUi>,
    OpenNote,
    BackPress {

    //region Views

    override val recyclerView = getView(R.id.recycler_view)

    private val rippleContainer = getView(R.id.ripple_container)
    private val logoView = getView(R.id.logo_view)
    private val buttonContainer = getView(R.id.button_container)

    private val disableButton = getView(R.id.disable_button)
    private val repeatButton = getView(R.id.repeat_button)
    private val moreButton = getView(R.id.more_button)

    override fun getItem(p: Int) = NoteItemUi(recyclerView, p)

    override val openNoteState: NoteState = NoteState.READ

    //endregion

    fun disable() {
        disableButton.click()
    }

    fun repeat(): Calendar {
        awaitMinuteEnd()

        repeatButton.click()
        return onRepeat()
    }

    fun waitRepeat(): Calendar {
        await(AlarmActivity.TIMEOUT_TIME)
        return onRepeat()
    }

    private fun onRepeat(): Calendar {
        val repeatArray = context.resources.getIntArray(R.array.pref_alarm_repeat_array)
        val repeatValue = repeatArray[preferencesRepo.repeat.ordinal]
        val calendar = getClearCalendar(repeatValue)

        if (dateList != null) {
            while (dateList.contains(calendar.toText())) {
                calendar.add(Calendar.MINUTE, 1)
            }
        }

        item.alarm.date = calendar.toText()

        return calendar
    }

    fun openMoreDialog(func: RepeatSheetDialogUi.() -> Unit = {}) {
        moreButton.click()
        RepeatSheetDialogUi(func)
    }

    fun noteLongClick(item: NoteItem) = getItem(p = 0).dialogClick(item)

    fun assert() = apply {
        parentContainer.isDisplayed()

        val rippleShade = RippleConverter().getRippleShade(theme)
        val fillColor = context.getAppSimpleColor(item.color, rippleShade)
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
            return AlarmScreen(item, dateList).assert().apply(func)
        }
    }
}