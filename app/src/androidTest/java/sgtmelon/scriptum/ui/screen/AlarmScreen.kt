package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.view.notification.AlarmActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Класс для ui контроля экрана [AlarmActivity]
 *
 * @author SerjantArbuz
 */
class AlarmScreen(noteModel: NoteModel) : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    companion object {
        operator fun invoke(func: AlarmScreen.() -> Unit, noteModel: NoteModel) =
                AlarmScreen(noteModel).apply {
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