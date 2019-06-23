package sgtmelon.scriptum.ui.screen

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

    companion object {
        operator fun invoke(func: NotificationScreen.() -> Unit, empty: Boolean) =
                NotificationScreen().apply {
                    assert { onDisplayContent(empty) }
                    func()
                }
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