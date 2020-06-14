package sgtmelon.scriptum.presentation.control.note.input

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.item.InputItem
import kotlin.random.Random

/**
 * Test for [InputControl].
 */
class InputControlTest : ParentTest() {

    private val inputControl by lazy { InputControl() }
    private val spyInputControl by lazy { spyk(inputControl) }

//    fun assert(func: Assert.() -> Unit) = Assert(inputControl).apply { func() }

    override fun setUp() {
        super.setUp()

        inputControl.logEnabled = false

        assertTrue(inputControl.list.isEmpty())
        assertEquals(InputControl.ND_POSITION, inputControl.position)
        assertTrue(inputControl.isEnabled)
    }

    /**
     * @Test fun `add changes to list and UNDO on not enable`() {
    TODO()

    inputControl.onRankChange(idFrom, psFrom, idTo, psTo)

    assert { undoFail() }
    }

    @Test fun `add changes to list and REDO on not enable`() {
    TODO()

    inputControl.apply {
    onRankChange(idFrom, psFrom, idTo, psTo)
    undo()
    }

    assert { redoFail() }
    }

    @Test fun `call UNDO on empty list`() {
    TODO()

    assert { undoFail() }
    }

    @Test fun `call REDO on empty list`() {
    TODO()

    assert { redoFail() }
    }

    @Test fun `call UNDO at extreme position`() {
    TODO()

    inputControl.apply {
    isEnabled = true
    onRankChange(idFrom, psFrom, idTo, psTo)
    undo()
    }

    assert { undoFail() }
    }

    @Test fun `call REDO at extreme position`() {
    TODO()

    inputControl.apply {
    isEnabled = true
    onRankChange(idFrom, psFrom, idTo, psTo)
    }

    assert { redoFail() }
    }

    @Test fun `call UNDO success`() {
    TODO()

    inputControl.apply {
    isEnabled = true
    onRankChange(idFrom, psFrom, idTo, psTo)
    }

    assert { undoSuccess() }
    }

    @Test fun `call REDO success`() {
    TODO()

    inputControl.apply {
    isEnabled = true
    onRankChange(idFrom, psFrom, idTo, psTo)
    undo()
    }

    assert { redoSuccess() }
    }

    @Test fun `remove list items after add position`() {
    TODO()

    inputControl.apply {
    isEnabled = true
    onRankChange(idFrom, psFrom, idTo, psTo)
    undo()
    onRankChange(idFrom, psFrom, idTo, psTo)
    }

    assert { redoFail() }
    }

    @Test fun `remove list items at start after max length`() {
    TODO()

    inputControl.apply {
    isEnabled = true

    repeat(times = INPUT_CONTROL_MAX_SIZE + 1) { onRankChange(idFrom, psFrom, idTo, psTo) }
    repeat(times = INPUT_CONTROL_MAX_SIZE) { inputControl.undo() }
    }

    assert { undoFail() }
    }

    @Test fun `input control reset`() {
    TODO()

    inputControl.apply {
    isEnabled = true
    onRankChange(idFrom, psFrom, idTo, psTo)
    reset()
    }

    assert { undoFail() }
    }

    companion object {
    private const val idFrom = 0L
    private const val psFrom = 0
    private const val idTo = 1L
    private const val psTo = 1
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
     */

    @Test fun isUndoAccess() {
        TODO()
    }

    @Test fun isRedoAccess() {
        TODO()
    }

    @Test fun access() {
        TODO()
    }

    @Test fun reset() {
        TODO()
    }

    @Test fun undo() {
        TODO()
    }

    @Test fun redo() {
        TODO()
    }

    @Test fun add() {
        TODO()
    }

    @Test fun remove() {
        TODO()
    }

    @Test fun makeNotEnabled() {
        inputControl.makeNotEnabled { assertFalse(inputControl.isEnabled) }

        assertTrue(inputControl.isEnabled)
    }

    @Test fun onRankChange() {
        val idFrom = Random.nextLong()
        val psFrom = Random.nextInt()
        val idTo = Random.nextLong()
        val psTo = Random.nextInt()

        val valueFrom = arrayOf(idFrom, psFrom).joinToString()
        val valueTo = arrayOf(idTo, psTo).joinToString()

        val inputItem = InputItem(InputAction.RANK, valueFrom, valueTo)

        every { spyInputControl.add(inputItem) } returns Unit

        spyInputControl.onRankChange(idFrom, psFrom, idTo, psTo)

        verifySequence {
            spyInputControl.onRankChange(idFrom, psFrom, idTo, psTo)
            spyInputControl.add(inputItem)
        }
    }

    @Test fun onColorChange() {
        val valueFrom = Random.nextInt()
        val valueTo = Random.nextInt()

        val inputItem = InputItem(InputAction.COLOR, valueFrom.toString(), valueTo.toString())

        every { spyInputControl.add(inputItem) } returns Unit

        spyInputControl.onColorChange(valueFrom, valueTo)

        verifySequence {
            spyInputControl.onColorChange(valueFrom, valueTo)
            spyInputControl.add(inputItem)
        }
    }

    @Test fun onNameChange() {
        val valueFrom = Random.nextString()
        val valueTo = Random.nextString()
        val cursor = mockk<InputItem.Cursor>()

        val inputItem = InputItem(InputAction.NAME, valueFrom, valueTo, cursor)

        every { spyInputControl.add(inputItem) } returns Unit

        spyInputControl.onNameChange(valueFrom, valueTo, cursor)

        verifySequence {
            spyInputControl.onNameChange(valueFrom, valueTo, cursor)
            spyInputControl.add(inputItem)
        }
    }

    @Test fun onTextChange() {
        val valueFrom = Random.nextString()
        val valueTo = Random.nextString()
        val cursor = mockk<InputItem.Cursor>()

        val inputItem = InputItem(InputAction.TEXT, valueFrom, valueTo, cursor)

        every { spyInputControl.add(inputItem) } returns Unit

        spyInputControl.onTextChange(valueFrom, valueTo, cursor)

        verifySequence {
            spyInputControl.onTextChange(valueFrom, valueTo, cursor)
            spyInputControl.add(inputItem)
        }
    }

    @Test fun onRollChange() {
        val p = Random.nextInt()
        val valueFrom = Random.nextString()
        val valueTo = Random.nextString()
        val cursor = mockk<InputItem.Cursor>()

        val inputItem = InputItem(InputAction.ROLL, valueFrom, valueTo, cursor, p)

        every { spyInputControl.add(inputItem) } returns Unit

        spyInputControl.onRollChange(p, valueFrom, valueTo, cursor)

        verifySequence {
            spyInputControl.onRollChange(p, valueFrom, valueTo, cursor)
            spyInputControl.add(inputItem)
        }
    }

    @Test fun onRollAdd() {
        val p = Random.nextInt()
        val valueTo = Random.nextString()

        val inputItem = InputItem(InputAction.ROLL_ADD, "", valueTo, null, p)

        every { spyInputControl.add(inputItem) } returns Unit

        spyInputControl.onRollAdd(p, valueTo)

        verifySequence {
            spyInputControl.onRollAdd(p, valueTo)
            spyInputControl.add(inputItem)
        }
    }

    @Test fun onRollRemove() {
        val p = Random.nextInt()
        val valueFrom = Random.nextString()

        val inputItem = InputItem(InputAction.ROLL_REMOVE, valueFrom, "", null, p)

        every { spyInputControl.add(inputItem) } returns Unit

        spyInputControl.onRollRemove(p, valueFrom)

        verifySequence {
            spyInputControl.onRollRemove(p, valueFrom)
            spyInputControl.add(inputItem)
        }
    }

    @Test fun onRollMove() {
        val valueFrom = Random.nextInt()
        val valueTo = Random.nextInt()

        val inputItem = InputItem(InputAction.ROLL_MOVE, valueFrom.toString(), valueTo.toString())

        every { spyInputControl.add(inputItem) } returns Unit

        spyInputControl.onRollMove(valueFrom, valueTo)

        verifySequence {
            spyInputControl.onRollMove(valueFrom, valueTo)
            spyInputControl.add(inputItem)
        }
    }

}