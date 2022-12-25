package sgtmelon.scriptum.cleanup.presentation.control.note.input

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.domain.model.item.HistoryItem
import sgtmelon.scriptum.cleanup.presentation.provider.BuildProvider
import sgtmelon.scriptum.data.noteHistory.NoteHistoryImpl
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [NoteHistoryImpl].
 */
class NoteHistoryImplTest : ParentTest() {

    private val history by lazy { NoteHistoryImpl() }
    private val spyHistory by lazy { spyk(history) }

    @Before override fun setUp() {
        super.setUp()

        assertTrue(history.list.isEmpty())
        assertEquals(NoteHistoryImpl.ND_POSITION, history.position)
        assertTrue(history.isEnabled)
    }


    @Test fun isUndoAccess() {
        assertFalse(history.isUndoAccess)

        history.position = Random.nextInt()
        assertFalse(history.isUndoAccess)

        history.list.addAll(nextList())
        history.position = NoteHistoryImpl.ND_POSITION
        assertFalse(history.isUndoAccess)

        history.position = Random.nextInt()
        assertTrue(history.isUndoAccess)
    }

    @Test fun isRedoAccess() {
        assertFalse(history.isRedoAccess)

        history.position = Random.nextInt()
        assertFalse(history.isRedoAccess)

        val list = nextList()
        history.list.addAll(list)
        history.position = list.lastIndex
        assertFalse(history.isRedoAccess)

        history.position = Random.nextInt()
        assertTrue(history.isRedoAccess)
    }

    @Test fun access() {
        val isUndoAccess = Random.nextBoolean()
        val isRedoAccess = Random.nextBoolean()
        val access = NoteHistoryImpl.Access(isUndoAccess, isRedoAccess)

        every { spyHistory.isUndoAccess } returns isUndoAccess
        every { spyHistory.isRedoAccess } returns isRedoAccess

        assertEquals(access, spyHistory.access)

        verifySequence {
            spyHistory.access
            spyHistory.isUndoAccess
            spyHistory.isRedoAccess
        }
    }

    @Test fun reset() {
        val list = nextList()

        history.list.addAll(list)
        history.position = Random.nextInt()

        history.reset()

        assertTrue(history.list.isEmpty())
        assertEquals(NoteHistoryImpl.ND_POSITION, history.position)
    }

    @Test fun undo() {
        val list = nextList()
        val position = list.indices.random()

        every { spyHistory.isUndoAccess } returns false
        assertNull(spyHistory.undo())
        assertEquals(NoteHistoryImpl.ND_POSITION, spyHistory.position)

        every { spyHistory.isUndoAccess } returns true
        assertNull(spyHistory.undo())
        assertEquals(NoteHistoryImpl.ND_POSITION - 1, spyHistory.position)

        spyHistory.list.addAll(list)
        spyHistory.position = position

        every { spyHistory.isUndoAccess } returns true
        assertEquals(list[position], spyHistory.undo())
        assertEquals(position - 1, spyHistory.position)

        verifySequence {
            spyHistory.undo()
            spyHistory.isUndoAccess
            spyHistory.position

            spyHistory.undo()
            spyHistory.isUndoAccess
            spyHistory.position

            spyHistory.list
            spyHistory.position = position

            spyHistory.undo()
            spyHistory.isUndoAccess
            spyHistory.position
        }
    }

    @Test fun redo() {
        val list = nextList()
        val position = (0 until list.lastIndex).random()

        every { spyHistory.isRedoAccess } returns false
        assertNull(spyHistory.redo())
        assertEquals(NoteHistoryImpl.ND_POSITION, spyHistory.position)

        every { spyHistory.isRedoAccess } returns true
        assertNull(spyHistory.redo())
        assertEquals(NoteHistoryImpl.ND_POSITION + 1, spyHistory.position)

        spyHistory.list.addAll(list)
        spyHistory.position = position

        every { spyHistory.isRedoAccess } returns true
        assertEquals(list[position + 1], spyHistory.redo())
        assertEquals(position + 1, spyHistory.position)

        verifySequence {
            spyHistory.redo()
            spyHistory.isRedoAccess
            spyHistory.position

            spyHistory.redo()
            spyHistory.isRedoAccess
            spyHistory.position

            spyHistory.list
            spyHistory.position = position

            spyHistory.redo()
            spyHistory.isRedoAccess
            spyHistory.position
        }
    }

    @Test fun add() {
        val firstItem = mockk<HistoryItem>()
        val secondItem = mockk<HistoryItem>()

        every { spyHistory.clearToPosition() } returns Unit
        every { spyHistory.clearToSize() } returns Unit
        every { spyHistory.listAll() } returns Unit

        spyHistory.isEnabled = false
        spyHistory.add(firstItem)

        spyHistory.isEnabled = true
        spyHistory.add(firstItem)

        assertEquals(listOf(firstItem), spyHistory.list)
        assertEquals(0, spyHistory.position)

        spyHistory.add(secondItem)

        assertEquals(listOf(firstItem, secondItem), spyHistory.list)
        assertEquals(1, spyHistory.position)

        verifySequence {
            spyHistory.isEnabled = false
            spyHistory.add(firstItem)
            spyHistory.isEnabled
            spyHistory.listAll()

            spyHistory.isEnabled = true
            spyHistory.add(firstItem)
            spyHistory.isEnabled
            spyHistory.clearToPosition()
            spyHistory.clearToSize()
            spyHistory.listAll()

            spyHistory.list
            spyHistory.position

            spyHistory.add(secondItem)
            spyHistory.isEnabled
            spyHistory.clearToPosition()
            spyHistory.clearToSize()
            spyHistory.listAll()

            spyHistory.list
            spyHistory.position
        }
    }

    @Test fun clearToPosition() {
        val list = nextList()
        val position = (0 until list.lastIndex).random()

        history.list.addAll(list)

        history.position = list.lastIndex
        history.clearToPosition()

        assertEquals(list, history.list)

        history.position = position
        history.clearToPosition()

        assertEquals(list.subList(0, position + 1), history.list)
    }

    @Test fun clearToSize() {
        val list = nextList()
        var position = Random.nextInt()
        val maxSize = (1..list.lastIndex).random()

        history.list.addAll(list)
        history.position = position

        mockkObject(BuildProvider)
        every { BuildProvider.noteHistoryMaxSize() } returns maxSize

        history.clearToSize()

        while (list.size >= maxSize) {
            list.removeAt(0)
            position--
        }

        assertEquals(list, history.list)
        assertEquals(position, history.position)
    }

    @Test fun onRankChange() {
        val idFrom = Random.nextLong()
        val psFrom = Random.nextInt()
        val idTo = Random.nextLong()
        val psTo = Random.nextInt()

        val valueFrom = arrayOf(idFrom, psFrom).joinToString()
        val valueTo = arrayOf(idTo, psTo).joinToString()

        val historyItem = HistoryItem(InputAction.RANK, valueFrom, valueTo)

        every { spyHistory.add(historyItem) } returns Unit

        spyHistory.onRankChange(idFrom, psFrom, idTo, psTo)

        verifySequence {
            spyHistory.onRankChange(idFrom, psFrom, idTo, psTo)
            spyHistory.add(historyItem)
        }
    }

    @Test fun onColorChange() {
        val valueFrom = mockk<Color>()
        val valueTo = mockk<Color>()
        val valueFromOrdinal = Random.nextInt()
        val valueToOrdinal = Random.nextInt()

        val historyItem = HistoryItem(
            InputAction.COLOR,
            valueFromOrdinal.toString(),
            valueToOrdinal.toString()
        )

        every { valueFrom.ordinal } returns valueFromOrdinal
        every { valueTo.ordinal } returns valueToOrdinal
        every { spyHistory.add(historyItem) } returns Unit

        spyHistory.onColorChange(valueFrom, valueTo)

        verifySequence {
            spyHistory.onColorChange(valueFrom, valueTo)
            valueFrom.ordinal
            valueTo.ordinal
            spyHistory.add(historyItem)
        }
    }

    @Test fun onNameChange() {
        val valueFrom = nextString()
        val valueTo = nextString()
        val cursor = mockk<HistoryItem.Cursor>()

        val historyItem = HistoryItem(InputAction.NAME, valueFrom, valueTo, cursor)

        every { spyHistory.add(historyItem) } returns Unit

        spyHistory.onNameChange(valueFrom, valueTo, cursor)

        verifySequence {
            spyHistory.onNameChange(valueFrom, valueTo, cursor)
            spyHistory.add(historyItem)
        }
    }

    @Test fun onTextChange() {
        val valueFrom = nextString()
        val valueTo = nextString()
        val cursor = mockk<HistoryItem.Cursor>()

        val historyItem = HistoryItem(InputAction.TEXT, valueFrom, valueTo, cursor)

        every { spyHistory.add(historyItem) } returns Unit

        spyHistory.onTextChange(valueFrom, valueTo, cursor)

        verifySequence {
            spyHistory.onTextChange(valueFrom, valueTo, cursor)
            spyHistory.add(historyItem)
        }
    }

    @Test fun onRollChange() {
        val p = Random.nextInt()
        val valueFrom = nextString()
        val valueTo = nextString()
        val cursor = mockk<HistoryItem.Cursor>()

        val historyItem = HistoryItem(InputAction.ROLL, valueFrom, valueTo, cursor, p)

        every { spyHistory.add(historyItem) } returns Unit

        spyHistory.onRollChange(p, valueFrom, valueTo, cursor)

        verifySequence {
            spyHistory.onRollChange(p, valueFrom, valueTo, cursor)
            spyHistory.add(historyItem)
        }
    }

    @Test fun onRollAdd() {
        val p = Random.nextInt()
        val valueTo = nextString()

        val historyItem = HistoryItem(InputAction.ROLL_ADD, "", valueTo, null, p)

        every { spyHistory.add(historyItem) } returns Unit

        spyHistory.onRollAdd(p, valueTo)

        verifySequence {
            spyHistory.onRollAdd(p, valueTo)
            spyHistory.add(historyItem)
        }
    }

    @Test fun onRollRemove() {
        val p = Random.nextInt()
        val valueFrom = nextString()

        val historyItem = HistoryItem(InputAction.ROLL_REMOVE, valueFrom, "", null, p)

        every { spyHistory.add(historyItem) } returns Unit

        spyHistory.onRollRemove(p, valueFrom)

        verifySequence {
            spyHistory.onRollRemove(p, valueFrom)
            spyHistory.add(historyItem)
        }
    }

    @Test fun onRollMove() {
        val valueFrom = Random.nextInt()
        val valueTo = Random.nextInt()

        val historyItem =
            HistoryItem(InputAction.ROLL_MOVE, valueFrom.toString(), valueTo.toString())

        every { spyHistory.add(historyItem) } returns Unit

        spyHistory.onRollMove(valueFrom, valueTo)

        verifySequence {
            spyHistory.onRollMove(valueFrom, valueTo)
            spyHistory.add(historyItem)
        }
    }

    private fun nextList() = MutableList<HistoryItem>(size = 5) { mockk() }

}