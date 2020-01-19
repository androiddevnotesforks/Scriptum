package sgtmelon.scriptum.ui.screen

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.extension.formatFuture
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.NotificationAdapter
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.InfoPage
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.part.InfoContainer
import sgtmelon.scriptum.ui.part.toolbar.SimpleToolbar
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [NotificationActivity].
 */
class NotificationScreen : ParentRecyclerScreen(R.id.notification_recycler), IPressBack {

    //region Views

    private val parentContainer = getViewById(R.id.notification_parent_container)
    private val toolbar = SimpleToolbar(R.string.title_notification)

    private val infoContainer = InfoContainer(InfoPage.NOTIFICATION)

    private fun getItem(p: Int) = Item(recyclerView, p)

    //endregion

    fun onClickClose() {
        toolbar.getToolbarButton().click()
    }

    fun openText(noteItem: NoteItem, p: Int = random, func: TextNoteScreen.() -> Unit = {}) {
        getItem(p).view.click()
        TextNoteScreen.invoke(func, State.READ, noteItem)
    }

    fun openRoll(noteItem: NoteItem, p: Int = random, func: RollNoteScreen.() -> Unit = {}) {
        getItem(p).view.click()
        RollNoteScreen.invoke(func, State.READ, noteItem)
    }

    fun onClickCancel(p: Int = random) = apply { getItem(p).cancelButton.click() }


    fun onAssertItem(p: Int, noteItem: NoteItem) {
        getItem(p).assert(noteItem)
    }

    fun assert(empty: Boolean) = apply {
        parentContainer.isDisplayed()
        toolbar.assert()

        infoContainer.assert(empty)
        recyclerView.isDisplayed(!empty)
    }

    /**
     * Class for UI control of [NotificationAdapter].
     */
    private class Item(listMatcher: Matcher<View>, p: Int) :
            ParentRecyclerItem<NoteItem>(listMatcher, p) {

        private val parentCard by lazy { getChild(getViewById(R.id.notification_parent_card)) }

        private val nameText by lazy { getChild(getViewById(R.id.notification_name_text)) }
        private val dateText by lazy { getChild(getViewById(R.id.notification_date_text)) }

        private val colorView by lazy { getChild(getViewById(R.id.notification_color_view)) }

        val cancelButton by lazy { getChild(getViewById(R.id.notification_cancel_button)) }

        override fun assert(item: NoteItem) {
            parentCard.isDisplayed().withCardBackground(R.attr.clBackgroundView)

            val name = if (item.name.isEmpty()) context.getString(R.string.hint_text_name) else item.name

            nameText.isDisplayed().withText(name, R.attr.clContent, R.dimen.text_16sp)

            val date = item.alarmDate.getCalendar().formatFuture(context)
            dateText.isDisplayed().withText(date, R.attr.clContentSecond, R.dimen.text_14sp)

            colorView.isDisplayed()
                    .withSize(widthId = R.dimen.layout_8dp)
                    .withColorIndicator(R.drawable.ic_color_indicator, theme, item.color)

            val description = context.getString(R.string.description_item_notification_cancel).plus(other = " ").plus(name)
                    .plus(context.getString(R.string.description_item_notification_cancel_at)).plus(other = " ").plus(item.alarmDate)

            cancelButton.isDisplayed()
                    .withDrawableAttr(R.drawable.ic_cancel_enter, R.attr.clContent)
                    .withContentDescription(description)
        }

    }

    companion object {
        operator fun invoke(func: NotificationScreen.() -> Unit,
                            empty: Boolean): NotificationScreen {
            return NotificationScreen().assert(empty).apply(func)
        }
    }

}