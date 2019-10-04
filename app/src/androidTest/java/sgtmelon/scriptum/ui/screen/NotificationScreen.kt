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
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.ui.ParentRecyclerItem
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.part.InfoContainer
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [NotificationActivity].
 */
class NotificationScreen : ParentRecyclerScreen(R.id.notification_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.notification_parent_container)
    private val toolbar = getToolbar(R.string.title_notification)

    private val infoContainer = InfoContainer(InfoPage.NOTIFICATION)

    private fun getItem(p: Int) = Item(recyclerView, p)

    //endregion

    fun onClickClose() {
        getToolbarButton().click()
    }

    fun openText(noteModel: NoteModel, p: Int = random, func: TextNoteScreen.() -> Unit = {}) {
        getItem(p).view.click()
        TextNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun openRoll(noteModel: NoteModel, p: Int = random, func: RollNoteScreen.() -> Unit = {}) {
        getItem(p).view.click()
        RollNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun onClickCancel(p: Int = random) = apply { getItem(p).cancelButton.click() }


    fun onAssertItem(p: Int, noteModel: NoteModel) {
        getItem(p).assert(noteModel)
    }

    fun assert(empty: Boolean) {
        parentContainer.isDisplayed()
        toolbar.isDisplayed()

        infoContainer.assert(empty)
        recyclerView.isDisplayed(!empty)
    }

    /**
     * Class for UI control of [NotificationAdapter].
     */
    private class Item(listMatcher: Matcher<View>, p: Int) :
            ParentRecyclerItem<NoteModel>(listMatcher, p) {

        private val colorView by lazy { getChild(getViewById(R.id.notification_color_view)) }

        private val nameText by lazy { getChild(getViewById(R.id.notification_name_text)) }
        private val dateText by lazy { getChild(getViewById(R.id.notification_date_text)) }

        val cancelButton by lazy { getChild(getViewById(R.id.notification_cancel_button)) }

        override fun assert(model: NoteModel) {
            colorView.isDisplayed().withColorIndicator(
                    R.drawable.ic_color_indicator, theme, model.noteEntity.color
            )

            nameText.isDisplayed().haveText(model.noteEntity.name)

            val date = model.alarmEntity.date.getCalendar().formatFuture(context)
            dateText.isDisplayed().haveText(date)

            cancelButton.isDisplayed().withDrawableAttr(
                    R.drawable.ic_cancel_enter, R.attr.clContent
            )
        }

    }

    companion object {
        operator fun invoke(func: NotificationScreen.() -> Unit, empty: Boolean) =
                NotificationScreen().apply { assert(empty) }.apply(func)
    }

}