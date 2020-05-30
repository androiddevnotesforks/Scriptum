package sgtmelon.scriptum.ui.screen

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.extension.formatFuture
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.SimpleInfoPage
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.adapter.NotificationAdapter
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.ui.IPressBack
import sgtmelon.scriptum.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.part.info.SimpleInfoContainer
import sgtmelon.scriptum.ui.part.panel.SnackbarPanel
import sgtmelon.scriptum.ui.part.toolbar.SimpleToolbar
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [NotificationActivity].
 */
class NotificationScreen : ParentRecyclerScreen(R.id.notification_recycler), IPressBack {

    //region Views

    private val parentContainer = getViewById(R.id.notification_parent_container)
    private val toolbar = SimpleToolbar(R.string.title_notification, withBack = true)

    private val infoContainer = SimpleInfoContainer(SimpleInfoPage.NOTIFICATION)

    fun getSnackbar(func: SnackbarPanel.() -> Unit): SnackbarPanel {
        val message = R.string.snackbar_message_notification
        val action = R.string.snackbar_action_cancel

        return SnackbarPanel(message, action, func)
    }

    private fun getItem(p: Int) = Item(recyclerView, p)

    //endregion

    fun onClickClose() {
        toolbar.getToolbarButton().click()
    }

    fun openText(noteItem: NoteItem.Text, p: Int? = random, isRankEmpty: Boolean = true,
                 func: TextNoteScreen.() -> Unit = {}) {
        if (p == null) return

        getItem(p).view.click()
        TextNoteScreen(func, State.READ, noteItem, isRankEmpty)
    }

    fun openRoll(noteItem: NoteItem.Roll, p: Int? = random, isRankEmpty: Boolean = true,
                 func: RollNoteScreen.() -> Unit = {}) {
        if (p == null) return

        getItem(p).view.click()
        RollNoteScreen(func, State.READ, noteItem, isRankEmpty)
    }

    fun onClickCancel(p: Int? = random, wait: Boolean = false) = apply {
        if (p == null) return@apply

        waitAfter(time = if (wait) SNACK_BAR_TIME else 0) {
            getItem(p).cancelButton.click()
            getSnackbar { assert() }
        }
    }


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