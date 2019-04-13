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

    @Test fun `add changes to list on not enable`() = with(inputControl) {
        onRankChange(valueFrom = listOf(0, 1), valueTo = listOf(0))
        assertNull(undo())
    }

    @Test fun `add changes to list on enable`() = with(inputControl) {
        setEnabled(true)
        onRankChange(valueFrom = listOf(0, 1), valueTo = listOf(0))
        assertNotNull(undo())
    }

    fun `call undo on empty list`() = assertNull(inputControl.undo())

    fun `call undo at first position`() = with(inputControl) {
        onColorChange(valueFrom = 0, valueTo = 1)
        undo()

        assertNull(undo())
    }

    fun `call undo on`() = with(inputControl) {
        // TODO
        onColorChange(valueFrom = 0, valueTo = 1)
        assertNotNull(undo())
    }

    fun `call redo on empty list`() = assertNull(inputControl.redo())

    fun `call redo on lat position`() = with(inputControl) {
        onColorChange(valueFrom = 0, valueTo = 1)
        assertNull(redo())
    }

    fun `call redo on`() = with(inputControl) {
        onColorChange(valueFrom = 0, valueTo = 1)
        undo()
        assertNotNull(redo())
    }

}