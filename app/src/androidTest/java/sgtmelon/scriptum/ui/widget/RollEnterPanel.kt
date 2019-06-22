package sgtmelon.scriptum.ui.widget

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.screen.note.INoteScreen
import sgtmelon.scriptum.ui.screen.note.RollNoteScreen

/**
 * Часть UI абстракции для [RollNoteScreen]
 *
 * @author SerjantArbuz
 */
class RollEnterPanel(private val callback: INoteScreen) : ParentUi() {

    // TODO Доступ через Text/Roll Note

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    fun onAddRoll(text: String) = action {
        onEnter(R.id.roll_note_enter, text)
        onClick(R.id.roll_note_add_button)
    }

    companion object {
        operator fun invoke(func: RollEnterPanel.() -> Unit, callback: INoteScreen) =
                RollEnterPanel(callback).apply {
                    // TODO assert
                    func()
                }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(state: State) {
            when (state) {
                State.READ, State.BIN -> {
                    notDisplay(R.id.roll_note_enter_container)
                    notDisplay(R.id.roll_note_enter)
                    notDisplay(R.id.roll_note_add_button)
                }
                State.EDIT, State.NEW -> {
                    onDisplay(R.id.roll_note_enter_container)
                    onDisplay(R.id.roll_note_enter)
                    onDisplay(R.id.roll_note_add_button)
                }
            }
        }

    }

}