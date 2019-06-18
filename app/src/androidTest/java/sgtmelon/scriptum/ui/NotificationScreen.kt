package sgtmelon.scriptum.ui

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.screen.view.notification.NotificationActivity
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

    fun onScrollThrough() = repeat(times = 2) {
        onScroll(Scroll.END, time = 4)
        onScroll(Scroll.START, time = 4)
    }

    fun openText(p: Int = positionRandom, func: TextNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        TextNoteScreen.invoke(State.READ, func)
    }

    fun openRoll(p: Int = positionRandom, func: RollNoteScreen.() -> Unit = {}) {
        onClickItem(p)
        RollNoteScreen.invoke(State.READ, func)
    }

    companion object {
        operator fun invoke(empty: Boolean, func: NotificationScreen.() -> Unit) =
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