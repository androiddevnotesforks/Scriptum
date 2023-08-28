package sgtmelon.scriptum.domain.useCase.note

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.data.noteHistory.model.HistoryAction
import sgtmelon.scriptum.data.noteHistory.model.HistoryChange
import sgtmelon.scriptum.domain.model.result.HistoryResult
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.test.common.nextString
import sgtmelon.tests.uniter.ParentTest
import kotlin.random.Random

/**
 * Test for [GetHistoryResultUseCase].
 */
class GetHistoryResultUseCaseTest : ParentTest() {

    private val getHistoryResult = GetHistoryResultUseCase()
    private val isUndo = Random.nextBoolean()

    @Test fun `action name`() {
        val action = mockk<HistoryAction.Name>()

        val valueChange = mockk<HistoryChange<String>>()
        val value = nextString()
        val cursorChange = mockk<HistoryChange<Int>>()
        val cursor = Random.nextInt()

        every { action.value } returns valueChange
        every { valueChange[isUndo] } returns value
        every { action.cursor } returns cursorChange
        every { cursorChange[isUndo] } returns cursor

        assertEquals(getHistoryResult(action, isUndo), HistoryResult.Name(value, cursor))

        verifySequence {
            action.value
            valueChange[isUndo]
            action.cursor
            cursorChange[isUndo]
        }
    }

    @Test fun `action rank`() {
        val action = mockk<HistoryAction.Rank>()

        val idChange = mockk<HistoryChange<Long>>()
        val id = Random.nextLong()
        val positionChange = mockk<HistoryChange<Int>>()
        val position = Random.nextInt()

        every { action.id } returns idChange
        every { idChange[isUndo] } returns id
        every { action.position } returns positionChange
        every { positionChange[isUndo] } returns position

        assertEquals(getHistoryResult(action, isUndo), HistoryResult.Rank(id, position))

        verifySequence {
            action.id
            idChange[isUndo]
            action.position
            positionChange[isUndo]
        }
    }

    @Test fun `action color`() {
        val action = mockk<HistoryAction.Color>()

        val valueChange = mockk<HistoryChange<Color>>()
        val value = Color.values().random()

        every { action.value } returns valueChange
        every { valueChange[isUndo] } returns value

        assertEquals(getHistoryResult(action, isUndo), HistoryResult.Color(value))

        verifySequence {
            action.value
            valueChange[isUndo]
        }
    }

    @Test fun `action text enter`() {
        val action = mockk<HistoryAction.Text.Enter>()

        val valueChange = mockk<HistoryChange<String>>()
        val value = nextString()
        val cursorChange = mockk<HistoryChange<Int>>()
        val cursor = Random.nextInt()

        every { action.value } returns valueChange
        every { valueChange[isUndo] } returns value
        every { action.cursor } returns cursorChange
        every { cursorChange[isUndo] } returns cursor

        assertEquals(getHistoryResult(action, isUndo), HistoryResult.Text.Enter(value, cursor))

        verifySequence {
            action.value
            valueChange[isUndo]
            action.cursor
            cursorChange[isUndo]
        }
    }

    @Test fun `action roll enter`() {
        val action = mockk<HistoryAction.Roll.Enter>()

        val p = Random.nextInt()
        val valueChange = mockk<HistoryChange<String>>()
        val value = nextString()
        val cursorChange = mockk<HistoryChange<Int>>()
        val cursor = Random.nextInt()

        every { action.p } returns p
        every { action.value } returns valueChange
        every { valueChange[isUndo] } returns value
        every { action.cursor } returns cursorChange
        every { cursorChange[isUndo] } returns cursor

        assertEquals(getHistoryResult(action, isUndo), HistoryResult.Roll.Enter(p, value, cursor))

        verifySequence {
            action.p
            action.value
            valueChange[isUndo]
            action.cursor
            cursorChange[isUndo]
        }
    }

    @Test fun `action roll list add isUndo`() {
        actionList(mockk<HistoryAction.Roll.List.Add>(), isUndo = true, isAdd = false)
    }

    @Test fun `action roll list add isRedo`() {
        actionList(mockk<HistoryAction.Roll.List.Add>(), isUndo = false, isAdd = true)
    }

    @Test fun `action roll list remove isUndo`() {
        actionList(mockk<HistoryAction.Roll.List.Remove>(), isUndo = true, isAdd = true)
    }

    @Test fun `action roll list remove isRedo`() {
        actionList(mockk<HistoryAction.Roll.List.Remove>(), isUndo = false, isAdd = false)
    }

    private fun actionList(action: HistoryAction.Roll.List, isUndo: Boolean, isAdd: Boolean) {
        val p = Random.nextInt()
        val item = mockk<RollItem>()

        every { action.p } returns p
        every { action.item } returns item

        if (isAdd) {
            assertEquals(getHistoryResult(action, isUndo), HistoryResult.Roll.Add(p, item))
        } else {
            assertEquals(getHistoryResult(action, isUndo), HistoryResult.Roll.Remove(p))
        }

        verifySequence {
            if (isAdd) {
                action.p
                action.item
            } else {
                action.p
            }
        }
    }

    @Test fun `action roll move`() {
        val action = mockk<HistoryAction.Roll.Move>()

        val valueChange = mockk<HistoryChange<Int>>()
        val valueFrom = Random.nextInt()
        val valueTo = Random.nextInt()

        every { action.value } returns valueChange
        every { valueChange[!isUndo] } returns valueFrom
        every { valueChange[isUndo] } returns valueTo

        assertEquals(getHistoryResult(action, isUndo), HistoryResult.Roll.Move(valueFrom, valueTo))

        verifySequence {
            action.value
            valueChange[!isUndo]
            action.value
            valueChange[isUndo]
        }
    }
}