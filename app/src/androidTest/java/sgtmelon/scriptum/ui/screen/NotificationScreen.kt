package sgtmelon.scriptum.ui.screen

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.basic.click
import sgtmelon.scriptum.basic.isDisplayed
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Class for UI control of [NotificationActivity]
 */
class NotificationScreen : ParentRecyclerScreen(R.id.notification_recycler) {

    //region Views

    private val parentContainer = getViewById(R.id.notification_parent_container)
    private val toolbar = getToolbar(R.string.title_notification)

    private val infoTitleText = getViewById(R.id.info_title_text).withText(R.string.info_notification_empty_title)
    private val infoDetailsText = getViewById(R.id.info_details_text).withText(R.string.info_notification_empty_details)

    //endregion

    fun onClickClose() {
        getToolbarButton().click()
    }

    fun openText(noteModel: NoteModel, p: Int = positionRandom,
                 func: TextNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        TextNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun openRoll(noteModel: NoteModel, p: Int = positionRandom,
                 func: RollNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        RollNoteScreen.invoke(func, State.READ, noteModel)
    }

    // TODO refactor
    fun onClickCancel(noteModel: NoteModel) = NotificationAction().apply {
        onClick(noteModel.noteEntity.name, R.id.notification_cancel_button)
    }


    // TODO refactor
    class NotificationAction {
        fun onClick(name: String, @IdRes childId: Int): ViewInteraction =
                onView(button(name, childId)).perform(click())

        private fun button(name: String, @IdRes childId: Int): Matcher<View> =
                allOf(withId(childId), withParent(allOf(
                        withId(R.id.notification_click_container), withChild(allOf(
                        withId(R.id.notification_content_container), withChild(withText(name))
                )))))
    }

    fun assert(empty: Boolean) {
        parentContainer.isDisplayed()
        toolbar.isDisplayed()

        infoTitleText.isDisplayed(empty)
        infoDetailsText.isDisplayed(empty)
        recyclerView.isDisplayed(!empty)
    }

    companion object {
        operator fun invoke(func: NotificationScreen.() -> Unit, empty: Boolean) =
                NotificationScreen().apply { assert(empty) }.apply(func)
    }

}