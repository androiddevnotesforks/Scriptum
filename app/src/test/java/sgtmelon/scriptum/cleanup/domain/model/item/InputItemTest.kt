package sgtmelon.scriptum.cleanup.domain.model.item

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.annotation.InputAction
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem.Cursor.Companion.get
import sgtmelon.scriptum.cleanup.parent.ParentTest

/**
 * Test for [InputItem].
 */
class InputItemTest : ParentTest() {

    //region Data

    private val valueFrom = "12345"
    private val valueTo = "54321"
    private val cursorFrom = 1
    private val cursorTo = 4

    private val cursor = InputItem.Cursor(cursorFrom, cursorTo)

    //endregion

    @Test fun defaultValues() {
        val item = InputItem(InputAction.COLOR, valueFrom, valueTo)

        assertEquals(InputItem.ND_CURSOR, item.cursor)
        assertEquals(InputItem.ND_POSITION, item.p)
    }


    @Test fun throwOnNameTag() {
        TODO()
//        assertThrows(NullPointerException::class.java) {
//            InputItem(InputAction.NAME, valueFrom, valueTo)
//        }
    }

    @Test fun notThrowOnNameTag() {
        InputItem(InputAction.NAME, valueFrom, valueTo, cursor)
    }

    @Test fun throwOnTextTag() {
        TODO()
//        assertThrows(NullPointerException::class.java) {
//            InputItem(InputAction.TEXT, valueFrom, valueTo)
//        }
    }

    @Test fun notThrowOnTextTag() {
        InputItem(InputAction.TEXT, valueFrom, valueTo, cursor)
    }

    @Test fun throwOnRollTag() {
        TODO()
//        assertThrows(NullPointerException::class.java) {
//            InputItem(InputAction.ROLL, valueFrom, valueTo)
//        }
    }

    @Test fun notThrowOnRollTag() {
        InputItem(InputAction.ROLL, valueFrom, valueTo, cursor)
    }


    @Test fun get() {
        val item = InputItem(InputAction.TEXT, valueFrom, valueTo, cursor)

        assertEquals(valueFrom, item[true])
        assertEquals(valueTo, item[false])
    }

    @Test fun cursorGet() {
        val item = InputItem(InputAction.TEXT, valueFrom, valueTo, cursor)

        assertEquals(cursorFrom, item.cursor[true])
        assertEquals(cursorTo, item.cursor[false])
    }

    @Test fun nullCursorGet() {
        val item = InputItem(InputAction.COLOR, valueFrom, valueTo)

        assertEquals(InputItem.Cursor.ND_VALUE, item.cursor[true])
        assertEquals(InputItem.Cursor.ND_VALUE, item.cursor[false])
    }
}