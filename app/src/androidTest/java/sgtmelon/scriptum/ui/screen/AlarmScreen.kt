package sgtmelon.scriptum.ui.screen

import androidx.test.espresso.Espresso.pressBackUnconditionally
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.waitBefore

/**
 * Class for UI control of [AlarmActivity]
 */
class AlarmScreen(private val noteModel: NoteModel) : ParentUi() {

    fun assert() = Assert()

    private fun openNote() = action { onClick(R.id.alarm_recycler, position = 0) }

    fun openTextNote(func: TextNoteScreen.() -> Unit = {}) {
        openNote()
        TextNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun openRollNote(func: RollNoteScreen.() -> Unit = {}) {
        openNote()
        RollNoteScreen.invoke(func, State.READ, noteModel)
    }

    fun onClickDisable() = action { onClick(R.id.alarm_disable_button) }

    fun onClickPostpone() = action { onClick(R.id.alarm_postpone_button) }

    fun onPressBack() = pressBackUnconditionally()


    //TODO больше onDisplay
    class Assert : BasicMatch() {
        init {
            onDisplay(R.id.alarm_parent_container)
            onDisplay(R.id.alarm_ripple_background)
            onDisplay(R.id.alarm_logo_view) // TODO #RELEASE2 content description
            onDisplay(R.id.alarm_recycler)

            onDisplay(R.id.alarm_button_container)
            onDisplay(R.id.alarm_disable_button)
            onDisplay(R.id.alarm_postpone_button)
        }
    }

    companion object {
        operator fun invoke(func: AlarmScreen.() -> Unit, noteModel: NoteModel) =
                AlarmScreen(noteModel).apply {
                    waitBefore(time = 1000) { assert() }
                    func()
                }
    }

}