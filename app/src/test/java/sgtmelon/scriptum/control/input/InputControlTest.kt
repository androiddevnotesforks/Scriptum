package sgtmelon.scriptum.control.input

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class InputControlTest {

    private val inputControl = InputControl()

    @Before fun setUp() {
        inputControl.logEnabled = false
    }

    @Test fun `add changes to list and UNDO on not enable`() {
        inputControl.onRankChange(valueFrom = listOf(0, 1), valueTo = listOf(0))

        assert { undoFail() }
    }

    @Test fun `add changes to list and REDO on not enable`() {
        inputControl.apply {
            onRankChange(valueFrom = listOf(0, 1), valueTo = listOf(0))
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
            onRankChange(rankValueFrom, rankValueTo)
            undo()
        }

        assert { undoFail() }
    }

    @Test fun `call REDO at extreme position`() {
        inputControl.apply {
            isEnabled = true
            onRankChange(rankValueFrom, rankValueTo)
        }

        assert { redoFail() }
    }

    @Test fun `call UNDO success`() {
        inputControl.apply {
            isEnabled = true
            onRankChange(rankValueFrom, rankValueTo)
        }

        assert { undoSuccess() }
    }

    @Test fun `call REDO success`() {
        inputControl.apply {
            isEnabled = true
            onRankChange(rankValueFrom, rankValueTo)
            undo()
        }

        assert { redoSuccess() }
    }

    @Test fun `remove list items after add position`() {
        inputControl.apply {
            isEnabled = true
            onRankChange(rankValueFrom, rankValueTo)
            undo()
            onRankChange(rankValueFrom, rankValueTo)
        }

        assert { redoFail() }
    }

    @Test fun `input control reset`() {
        inputControl.apply {
            isEnabled = true
            onRankChange(rankValueFrom, rankValueTo)
            reset()
        }

        assert { undoFail() }
    }

    fun assert(func: Assert.() -> Unit) = Assert(inputControl).apply { func() }

    class Assert(private val inputControl: InputControl) {

        fun undoFail() = assertNull(inputControl.undo())

        fun undoSuccess() = assertNotNull(inputControl.undo())

        fun redoFail() = assertNull(inputControl.redo())

        fun redoSuccess() = assertNotNull(inputControl.redo())

    }

    companion object {
        val rankValueFrom = listOf<Long>(0, 1, 2, 3)
        val rankValueTo = listOf<Long>(0, 1)
    }

}