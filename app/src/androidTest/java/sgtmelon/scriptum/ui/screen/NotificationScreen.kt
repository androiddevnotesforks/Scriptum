package sgtmelon.scriptum.ui.screen

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.haveText
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.withDrawable
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
 * Class for UI control of [NotificationActivity]
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

    private inner class Item(list: Matcher<View>, p: Int) : ParentRecyclerItem<NoteModel>(list, p) {

        private val colorView by lazy { getChild(getViewById(R.id.notification_color_view)) }

        // TODO get with text
        private val nameText by lazy { getChild(getViewById(R.id.notification_name_text)) }
        private val dateText by lazy { getChild(getViewById(R.id.notification_date_text)) }

        val cancelButton by lazy { getChild(getViewById(R.id.notification_cancel_button)) }

        // TODO have src / color
        override fun assert(model: NoteModel) {
            colorView.isDisplayed()

            nameText.isDisplayed().haveText(model.noteEntity.name)
            dateText.isDisplayed()

            cancelButton.isDisplayed().withDrawable(R.drawable.ic_cancel_enter, R.attr.clContent)
        }

    }

    companion object {
        operator fun invoke(func: NotificationScreen.() -> Unit, empty: Boolean) =
                NotificationScreen().apply { assert(empty) }.apply(func)
    }

}