package sgtmelon.scriptum.ui.screen

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.view.notification.NotificationActivity
import sgtmelon.scriptum.ui.ParentRecyclerScreen
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen

/**
 * Класс для ui контроля экрана [NotificationActivity]
 *
 * @author SerjantArbuz
 */
class NotificationScreen : ParentRecyclerScreen(R.id.notification_recycler) {

    private fun notificationAction(func: NotificationAction.() -> Unit) =
            NotificationAction().apply { func() }

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

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

    fun onClickCancel(noteModel: NoteModel) = notificationAction {
        onClick(noteModel.noteEntity.name, R.id.notification_cancel_button)
    }

    companion object {
        operator fun invoke(func: NotificationScreen.() -> Unit, empty: Boolean) =
                NotificationScreen().apply {
                    assert { onDisplayContent(empty) }
                    func()
                }
    }

    class NotificationAction {

        fun onClick(name: String, @IdRes childId: Int): ViewInteraction =
                onView(rankButton(name, childId)).perform(click())

        private fun rankButton(name: String, @IdRes childId: Int): Matcher<View> =
                CoreMatchers.allOf(ViewMatchers.withId(childId), ViewMatchers.withParent(CoreMatchers.allOf(
                        ViewMatchers.withId(R.id.rank_click_container),
                        ViewMatchers.withChild(CoreMatchers.allOf(ViewMatchers.withId(R.id.rank_content_container), ViewMatchers.withChild(ViewMatchers.withText(name))))
                )))
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(empty: Boolean) {
            onDisplay(R.id.notification_parent_container)

            onDisplayToolbar(R.id.toolbar_container, R.string.title_notification)

            if (empty) {
                onDisplay(R.id.info_title_text, R.string.info_notification_title)
                onDisplay(R.id.info_details_text, R.string.info_notification_details)
                notDisplay(R.id.notification_recycler)
            } else {
                notDisplay(R.id.info_title_text, R.string.info_notification_title)
                notDisplay(R.id.info_details_text, R.string.info_notification_details)
                onDisplay(R.id.notification_recycler)
            }
        }

    }

}