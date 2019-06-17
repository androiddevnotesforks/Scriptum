package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.view.notification.AlarmActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Класс для ui контроля экрана [AlarmActivity]
 *
 * @author SerjantArbuz
 */
class AlarmScreen(noteEntity: NoteEntity) : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    companion object {
        operator fun invoke(noteEntity: NoteEntity, func: AlarmScreen.() -> Unit) =
                AlarmScreen(noteEntity).apply {
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