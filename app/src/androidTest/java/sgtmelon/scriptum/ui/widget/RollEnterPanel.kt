package sgtmelon.scriptum.ui.widget

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class RollEnterPanel : ParentUi() {

    // TODO Доступ через Text/Roll Note

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    companion object {
        operator fun invoke(func: RollEnterPanel.() -> Unit) = RollEnterPanel().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(state: State) {
            when (state) {
                State.READ, State.BIN -> {
                    doesNotDisplay(R.id.roll_note_enter_container)
                    doesNotDisplay(R.id.roll_note_enter)
                    doesNotDisplay(R.id.roll_note_add_button)
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