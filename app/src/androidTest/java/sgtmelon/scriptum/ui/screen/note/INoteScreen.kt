package sgtmelon.scriptum.ui.screen.note

import org.junit.Assert.assertTrue
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.ui.ParentUi

/**
 * Interface for communication child ui abstractions with [TextNoteScreen] and [RollNoteScreen]
 */
interface INoteScreen<T : ParentUi>  {

    // TODO #TEST add exit from screen control

    var state: State

    var noteItem: NoteItem

    var shadowItem: NoteItem

    val isRankEmpty: Boolean

    val inputControl: InputControl

    fun fullAssert(): T

    fun throwOnWrongState(vararg actual: State, func: () -> Unit) {
        assertTrue(actual.contains(state))
        func()
    }

}