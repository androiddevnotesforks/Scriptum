package sgtmelon.scriptum.data.noteHistory

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.data.noteHistory.HistoryItem.Cursor.Companion.get
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

    @Test fun todo() {
        TODO("Update tests")
    }

    @Test fun defaultValues() {
        val item = HistoryItem(HistoryAction.COLOR, valueFrom, valueTo)

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
        HistoryItem(HistoryAction.NAME, valueFrom, valueTo, cursor)
    }

    @Test fun throwOnTextTag() {
        TODO()
//        assertThrows(NullPointerException::class.java) {
//            InputItem(InputAction.TEXT, valueFrom, valueTo)
//        }
    }

    @Test fun notThrowOnTextTag() {
        HistoryItem(HistoryAction.TEXT, valueFrom, valueTo, cursor)
    }

    @Test fun throwOnRollTag() {
        TODO()
//        assertThrows(NullPointerException::class.java) {
//            InputItem(InputAction.ROLL, valueFrom, valueTo)
//        }
    }

    @Test fun notThrowOnRollTag() {
        HistoryItem(HistoryAction.ROLL, valueFrom, valueTo, cursor)
    }


    @Test fun get() {
        val item = HistoryItem(HistoryAction.TEXT, valueFrom, valueTo, cursor)

        assertEquals(valueFrom, item[true])
        assertEquals(valueTo, item[false])
    }

    @Test fun cursorGet() {
        val item = HistoryItem(HistoryAction.TEXT, valueFrom, valueTo, cursor)

        assertEquals(cursorFrom, item.cursor[true])
        assertEquals(cursorTo, item.cursor[false])
    }

    @Test fun nullCursorGet() {
        val item = HistoryItem(HistoryAction.COLOR, valueFrom, valueTo)

        assertEquals(HistoryItem.Cursor.ND_VALUE, item.cursor[true])
        assertEquals(HistoryItem.Cursor.ND_VALUE, item.cursor[false])
    }
}