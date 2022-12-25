package sgtmelon.scriptum.cleanup.domain.model.item

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.data.noteHistory.HistoryItem
import sgtmelon.scriptum.data.noteHistory.HistoryItem.Cursor.Companion.get
import sgtmelon.scriptum.data.noteHistory.InputAction
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [HistoryItem].
 */
class HistoryItemTest : ParentTest() {

    //region Data

    private val valueFrom = "12345"
    private val valueTo = "54321"
    private val cursorFrom = 1
    private val cursorTo = 4

    private val cursor = HistoryItem.Cursor(cursorFrom, cursorTo)

    //endregion

    @Test fun defaultValues() {
        val item = HistoryItem(InputAction.COLOR, valueFrom, valueTo)

        assertEquals(HistoryItem.ND_CURSOR, item.cursor)
        assertEquals(HistoryItem.ND_POSITION, item.p)
    }


    @Test fun throwOnNameTag() {
        TODO()
//        assertThrows(NullPointerException::class.java) {
//            InputItem(InputAction.NAME, valueFrom, valueTo)
//        }
    }

    @Test fun notThrowOnNameTag() {
        HistoryItem(InputAction.NAME, valueFrom, valueTo, cursor)
    }

    @Test fun throwOnTextTag() {
        TODO()
//        assertThrows(NullPointerException::class.java) {
//            InputItem(InputAction.TEXT, valueFrom, valueTo)
//        }
    }

    @Test fun notThrowOnTextTag() {
        HistoryItem(InputAction.TEXT, valueFrom, valueTo, cursor)
    }

    @Test fun throwOnRollTag() {
        TODO()
//        assertThrows(NullPointerException::class.java) {
//            InputItem(InputAction.ROLL, valueFrom, valueTo)
//        }
    }

    @Test fun notThrowOnRollTag() {
        HistoryItem(InputAction.ROLL, valueFrom, valueTo, cursor)
    }


    @Test fun get() {
        val item = HistoryItem(InputAction.TEXT, valueFrom, valueTo, cursor)

        assertEquals(valueFrom, item[true])
        assertEquals(valueTo, item[false])
    }

    @Test fun cursorGet() {
        val item = HistoryItem(InputAction.TEXT, valueFrom, valueTo, cursor)

        assertEquals(cursorFrom, item.cursor[true])
        assertEquals(cursorTo, item.cursor[false])
    }

    @Test fun nullCursorGet() {
        val item = HistoryItem(InputAction.COLOR, valueFrom, valueTo)

        assertEquals(HistoryItem.Cursor.ND_VALUE, item.cursor[true])
        assertEquals(HistoryItem.Cursor.ND_VALUE, item.cursor[false])
    }
}