package sgtmelon.scriptum.presentation.control.note.input

import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.item.InputItem
import sgtmelon.scriptum.parent.ParentTest
import sgtmelon.scriptum.presentation.provider.BuildProvider
import kotlin.random.Random

/**
 * Test for [InputControl].
 */
class InputControlTest : ParentTest() {

    private val inputControl by lazy { InputControl() }
    private val spyInputControl by lazy { spyk(inputControl) }

    @Before override fun setup() {
        super.setup()

        assertTrue(inputControl.list.isEmpty())
        assertEquals(InputControl.ND_POSITION, inputControl.position)
        assertTrue(inputControl.isEnabled)
    }


    @Test fun isUndoAccess() {
        assertFalse(inputControl.isUndoAccess)

        inputControl.position = Random.nextInt()
        assertFalse(inputControl.isUndoAccess)

        inputControl.list.addAll(nextList())
        inputControl.position = InputControl.ND_POSITION
        assertFalse(inputControl.isUndoAccess)

        inputControl.position = Random.nextInt()
        assertTrue(inputControl.isUndoAccess)
    }

    @Test fun isRedoAccess() {
        assertFalse(inputControl.isRedoAccess)

        inputControl.position = Random.nextInt()
        assertFalse(inputControl.isRedoAccess)

        val list = nextList()
        inputControl.list.addAll(list)
        inputControl.position = list.lastIndex
        assertFalse(inputControl.isRedoAccess)

        inputControl.position = Random.nextInt()
        assertTrue(inputControl.isRedoAccess)
    }

    @Test fun access() {
        val isUndoAccess = Random.nextBoolean()
        val isRedoAccess = Random.nextBoolean()
        val access = InputControl.Access(isUndoAccess, isRedoAccess)

        every { spyInputControl.isUndoAccess } returns isUndoAccess
        every { spyInputControl.isRedoAccess } returns isRedoAccess

        assertEquals(access, spyInputControl.access)

        verifySequence {
            spyInputControl.access
            spyInputControl.isUndoAccess
            spyInputControl.isRedoAccess
        }
    }

    @Test fun reset() {
        val list = nextList()

        inputControl.list.addAll(list)
        inputControl.position = Random.nextInt()

        inputControl.reset()

        assertTrue(inputControl.list.isEmpty())
        assertEquals(InputControl.ND_POSITION, inputControl.position)
    }

    @Test fun undo() {
        val list = nextList()
        val position = list.indices.random()

        every { spyInputControl.isUndoAccess } returns false
        assertNull(spyInputControl.undo())
        assertEquals(InputControl.ND_POSITION, spyInputControl.position)

        every { spyInputControl.isUndoAccess } returns true
        assertNull(spyInputControl.undo())
        assertEquals(InputControl.ND_POSITION - 1, spyInputControl.position)

        spyInputControl.list.addAll(list)
        spyInputControl.position = position

        every { spyInputControl.isUndoAccess } returns true
        assertEquals(list[position], spyInputControl.undo())
        assertEquals(position - 1, spyInputControl.position)

        verifySequence {
            spyInputControl.undo()
            spyInputControl.isUndoAccess
            spyInputControl.position

            spyInputControl.undo()
            spyInputControl.isUndoAccess
            spyInputControl.position

            spyInputControl.list
            spyInputControl.position = position

            spyInputControl.undo()
            spyInputControl.isUndoAccess
            spyInputControl.position
        }
    }

    @Test fun redo() {
        val list = nextList()
        val position = (0 until list.lastIndex).random()

        every { spyInputControl.isRedoAccess } returns false
        assertNull(spyInputControl.redo())
        assertEquals(InputControl.ND_POSITION, spyInputControl.position)

        every { spyInputControl.isRedoAccess } returns true
        assertNull(spyInputControl.redo())
        assertEquals(InputControl.ND_POSITION + 1, spyInputControl.position)

        spyInputControl.list.addAll(list)
        spyInputControl.position = position

        every { spyInputControl.isRedoAccess } returns true
        assertEquals(list[position + 1], spyInputControl.redo())
        assertEquals(position + 1, spyInputControl.position)

        verifySequence {
            spyInputControl.redo()
            spyInputControl.isRedoAccess
            spyInputControl.position

            spyInputControl.redo()
            spyInputControl.isRedoAccess
            spyInputControl.position

            spyInputControl.list
            spyInputControl.position = position

            spyInputControl.redo()
            spyInputControl.isRedoAccess
            spyInputControl.position
        }
    }

    @Test fun add() {
        val firstItem = mockk<InputItem>()
        val secondItem = mockk<InputItem>()

        every { spyInputControl.clearToPosition() } returns Unit
        every { spyInputControl.clearToSize() } returns Unit
        every { spyInputControl.listAll() } returns Unit

        spyInputControl.isEnabled = false
        spyInputControl.add(firstItem)

        spyInputControl.isEnabled = true
        spyInputControl.add(firstItem)

        assertEquals(listOf(firstItem), spyInputControl.list)
        assertEquals(0, spyInputControl.position)

        spyInputControl.add(secondItem)

        assertEquals(listOf(firstItem, secondItem), spyInputControl.list)
        assertEquals(1, spyInputControl.position)

        verifySequence {
            spyInputControl.isEnabled = false
            spyInputControl.add(firstItem)
            spyInputControl.isEnabled
            spyInputControl.listAll()

            spyInputControl.isEnabled = true
            spyInputControl.add(firstItem)
            spyInputControl.isEnabled
            spyInputControl.clearToPosition()
            spyInputControl.clearToSize()
            spyInputControl.listAll()

            spyInputControl.list
            spyInputControl.position

            spyInputControl.add(secondItem)
            spyInputControl.isEnabled
            spyInputControl.clearToPosition()
            spyInputControl.clearToSize()
            spyInputControl.listAll()

            spyInputControl.list
            spyInputControl.position
        }
    }

    @Test fun clearToPosition() {
        val list = nextList()
        val position = (0 until list.lastIndex).random()

        inputControl.list.addAll(list)

        inputControl.position = list.lastIndex
        inputControl.clearToPosition()

        assertEquals(list, inputControl.list)

        inputControl.position = position
        inputControl.clearToPosition()

        assertEquals(list.subList(0, position + 1), inputControl.list)
    }

    @Test fun clearToSize() {
        val list = nextList()
        var position = Random.nextInt()
        val maxSize = (1..list.lastIndex).random()

        inputControl.list.addAll(list)
        inputControl.position = position

        mockkObject(BuildProvider)
        every { BuildProvider.inputControlMaxSize() } returns maxSize

        inputControl.clearToSize()

        while(list.size >= maxSize) {
            list.removeAt(0)
            position--
        }

        assertEquals(list, inputControl.list)
        assertEquals(position, inputControl.position)
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
        val valueFrom = nextString()
        val valueTo = nextString()
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
        val valueFrom = nextString()
        val valueTo = nextString()
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
        val valueFrom = nextString()
        val valueTo = nextString()
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
        val valueTo = nextString()

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
        val valueFrom = nextString()

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

    private fun nextList() = MutableList<InputItem>(size = 5) { mockk() }

}