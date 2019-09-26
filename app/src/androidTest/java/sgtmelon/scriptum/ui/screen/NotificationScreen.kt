package sgtmelon.scriptum.ui.screen

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
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

    private fun getItem(noteModel: NoteModel) = Item(recyclerView, noteModel)

    //endregion

    fun onClickClose() {
        getToolbarButton().click()
    }

    fun openText(noteModel: NoteModel, p: Int = random,
                 func: TextNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        TextNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun openRoll(noteModel: NoteModel, p: Int = random,
                 func: RollNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        RollNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun onClickCancel(noteModel: NoteModel) {
        getItem(noteModel).cancelButton.click()
    }


    fun onAssertItem(noteModel: NoteModel) {
        getItem(noteModel).assert()
    }

    fun assert(empty: Boolean) {
        parentContainer.isDisplayed()
        toolbar.isDisplayed()

        infoContainer.assert(empty)
        recyclerView.isDisplayed(!empty)
    }

    private inner class Item(list: Matcher<View>, noteModel: NoteModel) :
            ParentRecyclerItem(list, hasDescendant(
                    getView(R.id.notification_name_text, noteModel.noteEntity.name)
            )) {

        private val colorView by lazy { getChild(getViewById(R.id.notification_color_view)) }

        private val nameText by lazy { getChild(getView(R.id.notification_name_text, noteModel.noteEntity.name)) }
        private val dateText by lazy { getChild(getViewById(R.id.notification_date_text)) } // todo get with text

        val cancelButton by lazy { getChild(getViewById(R.id.notification_cancel_button)) }

        // TODO have src / color
        fun assert() {
            colorView.isDisplayed()

            nameText.isDisplayed()
            dateText.isDisplayed()

            cancelButton.isDisplayed()
        }

    }

    companion object {
        operator fun invoke(func: NotificationScreen.() -> Unit, empty: Boolean) =
                NotificationScreen().apply { assert(empty) }.apply(func)
    }

}