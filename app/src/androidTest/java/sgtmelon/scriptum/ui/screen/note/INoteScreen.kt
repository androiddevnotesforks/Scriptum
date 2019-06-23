package sgtmelon.scriptum.ui.screen.note

import org.junit.Assert.assertTrue
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.NoteModel

/**
 * Интерфейс для общения дочерних ui абстракций с [TextNoteScreen] и [RollNoteScreen]
 *
 * @author SerjantArbuz
 */
interface INoteScreen {

    // TODO добавить контроль выхода с экрана

    var state: State

    var noteModel: NoteModel

    var shadowModel: NoteModel

    val isRankEmpty: Boolean

    val inputControl: InputControl

    fun fullAssert()

    fun throwOnWrongState(vararg actual: State, func: () -> Unit) {
        assertTrue(actual.contains(state))
        func()
    }

}