package sgtmelon.scriptum.ui.screen.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

/**
 * Часть UI абстракции для [RollNoteScreen]
 *
 * @author SerjantArbuz
 */
class RollEnterPanel(private val callback: INoteScreen) : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert(callback).apply { func() }

    fun onAddRoll(text: String) = action {
        onEnter(R.id.roll_note_enter, text)
        onClick(R.id.roll_note_add_button)
    }

    companion object {
        operator fun invoke(func: RollEnterPanel.() -> Unit, callback: INoteScreen) =
                RollEnterPanel(callback).apply {
                    assert { onDisplayContent() }
                    func()
                }
    }

    class Assert(private val callback: INoteScreen) : BasicMatch() {

        // tODO assert Доступа к кнопке (при отсутствии текста / при введённом тексте)

        fun onDisplayContent(): Unit = with(callback) {
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