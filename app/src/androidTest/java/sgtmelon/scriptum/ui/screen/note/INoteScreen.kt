package sgtmelon.scriptum.ui.screen.note

import org.junit.Assert
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel

/**
 * Интерфейс для общения дочерних ui абстракций с [TextNoteScreen] и [RollNoteScreen]
 *
 * @author SerjantArbuz
 */
interface INoteScreen {

    var state: State

    val noteModel: NoteModel

    val inputControl: InputControl

    // TODO придумать, что делать с NEW
    fun throwOnWrongState(vararg actual: State, func: () -> Unit) {
        Assert.assertEquals(state, actual)
        func()
    }

}