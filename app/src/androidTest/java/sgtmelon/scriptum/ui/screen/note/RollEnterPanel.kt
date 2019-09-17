package sgtmelon.scriptum.ui.screen.note

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import kotlin.random.Random

/**
 * Часть UI абстракции для [RollNoteScreen]
 */
class RollEnterPanel(private val callback: INoteScreen) : ParentUi() {

    private var enterText: String = ""

    fun assert() = Assert(callback, enterText)

    fun onAddRoll(text: String) {
        onEnter(text)

        enterText = text
        assert()

        onClickAdd()
    }

    private fun onEnter(text: String) = action { onEnter(R.id.roll_note_enter, text) }

    private fun onClickAdd(longClick: Boolean = Random.nextBoolean()) = action {
        if (longClick) {
            onClick(R.id.roll_note_add_button)
        } else {
            onLongClick(R.id.roll_note_add_button)
        }

        enterText = ""
        assert()
    }


    class Assert(callback: INoteScreen, enterText: String) : BasicMatch() {
        init {
            callback.apply {
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

            isEnabled(R.id.roll_note_add_button, enterText.isNotEmpty())
        }
    }

    companion object {
        operator fun invoke(func: RollEnterPanel.() -> Unit, callback: INoteScreen) =
                RollEnterPanel(callback).apply {
                    assert()
                    func()
                }
    }

}