package sgtmelon.scriptum.ui.screen.note

import org.junit.Assert.assertTrue
import sgtmelon.scriptum.data.State
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.ui.ParentUi

/**
 * Interface for communication child ui abstractions with [TextNoteScreen] and [RollNoteScreen]
 */
interface INoteScreen<T : ParentUi>  {

    // TODO #TEST add exit from screen control

    var state: State

    var noteItem: NoteItem

    /**
     * Item for changes in edit mode.
     */
    var shadowItem: NoteItem

    val isRankEmpty: Boolean

    val inputControl: InputControl

    fun fullAssert(): T

    fun throwOnWrongState(vararg actual: State, func: () -> Unit) {
        assertTrue(actual.contains(state))
        func()
    }

}