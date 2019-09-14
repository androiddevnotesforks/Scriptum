package sgtmelon.scriptum.control.input

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.BuildConfig.INPUT_CONTROL_MAX_SIZE

/**
 * Test for [InputControl]
 */
class InputControlTest {

    private val inputControl = InputControl()

    fun assert(func: Assert.() -> Unit) = Assert(inputControl).apply { func() }

    @Before fun setUp() {
        inputControl.logEnabled = false
    }

    @Test fun `add changes to list and UNDO on not enable`() {
        inputControl.onRankChange(idFrom, psFrom, idTo, psTo)

        assert { undoFail() }
    }

    @Test fun `add changes to list and REDO on not enable`() {
        inputControl.apply {
            onRankChange(idFrom, psFrom, idTo, psTo)
            undo()
        }

        assert { redoFail() }
    }

    @Test fun `call UNDO on empty list`() {
        assert { undoFail() }
    }

    @Test fun `call REDO on empty list`() {
        assert { redoFail() }
    }

    @Test fun `call UNDO at extreme position`() {
        inputControl.apply {
            isEnabled = true
            onRankChange(idFrom, psFrom, idTo, psTo)
            undo()
        }

        assert { undoFail() }
    }

    @Test fun `call REDO at extreme position`() {
        inputControl.apply {
            isEnabled = true
            onRankChange(idFrom, psFrom, idTo, psTo)
        }

        assert { redoFail() }
    }

    @Test fun `call UNDO success`() {
        inputControl.apply {
            isEnabled = true
            onRankChange(idFrom, psFrom, idTo, psTo)
        }

        assert { undoSuccess() }
    }

    @Test fun `call REDO success`() {
        inputControl.apply {
            isEnabled = true
            onRankChange(idFrom, psFrom, idTo, psTo)
            undo()
        }

        assert { redoSuccess() }
    }

    @Test fun `remove list items after add position`() {
        inputControl.apply {
            isEnabled = true
            onRankChange(idFrom, psFrom, idTo, psTo)
            undo()
            onRankChange(idFrom, psFrom, idTo, psTo)
        }

        assert { redoFail() }
    }

    @Test fun `remove list items at start after max length`() {
        inputControl.apply {
            isEnabled = true

            repeat(times = INPUT_CONTROL_MAX_SIZE + 1) { onRankChange(idFrom, psFrom, idTo, psTo) }
            repeat(times = INPUT_CONTROL_MAX_SIZE) { inputControl.undo() }
        }

        assert { undoFail() }
    }

    @Test fun `input control reset`() {
        inputControl.apply {
            isEnabled = true
            onRankChange(idFrom, psFrom, idTo, psTo)
            reset()
        }

        assert { undoFail() }
    }

    private companion object {
        const val idFrom = 0L
        const val psFrom = 0
        const val idTo = 1L
        const val psTo = 1
    }

    class Assert(private val inputControl: InputControl) {

        fun undoFail() {
            assertFalse(inputControl.isUndoAccess)
            assertNull(inputControl.undo())
        }

        fun undoSuccess() {
            assertTrue(inputControl.isUndoAccess)
            assertNotNull(inputControl.undo())
        }

        fun redoFail() {
            assertFalse(inputControl.isRedoAccess)
            assertNull(inputControl.redo())
        }

        fun redoSuccess() {
            assertTrue(inputControl.isRedoAccess)
            assertNotNull(inputControl.redo())
        }

    }

}