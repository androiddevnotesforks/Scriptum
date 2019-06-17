package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.room.entity.NoteItem
import sgtmelon.scriptum.screen.view.notification.AlarmActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Класс для ui контроля экрана [AlarmActivity]
 *
 * @author SerjantArbuz
 */
class AlarmScreen(noteItem: NoteItem) : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    companion object {
        operator fun invoke(noteItem: NoteItem, func: AlarmScreen.() -> Unit) =
                AlarmScreen(noteItem).apply {
                    assert { onDisplayContent() }
                    func()
                }
    }

    class Assert : BasicMatch() {

        //TODO больше onDisplay
        fun onDisplayContent() {
            onDisplay(R.id.alarm_parent_container)
        }

    }

}