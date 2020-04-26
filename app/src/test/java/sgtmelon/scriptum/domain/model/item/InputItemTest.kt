package sgtmelon.scriptum.domain.model.item

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.domain.model.annotation.InputAction
import sgtmelon.scriptum.domain.model.item.InputItem.Cursor.Companion.get

/**
 * Test for [InputItem].
 */
class InputItemTest : ParentTest() {


    @Test fun defaultValues() {
        val item = InputItem(InputAction.COLOR, VALUE_FROM, VALUE_TO)

        assertEquals(InputItem.ND_CURSOR, item.cursor)
        assertEquals(InputItem.ND_POSITION, item.p)
    }


    @Test fun throwOnNameTag() {
        assertThrows(NullPointerException::class.java) {
            InputItem(InputAction.NAME, VALUE_FROM, VALUE_TO)
        }
    }

    @Test fun notThrowOnNameTag() {
        InputItem(InputAction.NAME, VALUE_FROM, VALUE_TO, cursor)
    }

    @Test fun throwOnTextTag() {
        assertThrows(NullPointerException::class.java) {
            InputItem(InputAction.TEXT, VALUE_FROM, VALUE_TO)
        }
    }

    @Test fun notThrowOnTextTag() {
        InputItem(InputAction.TEXT, VALUE_FROM, VALUE_TO, cursor)
    }

    @Test fun throwOnRollTag() {
        assertThrows(NullPointerException::class.java) {
            InputItem(InputAction.ROLL, VALUE_FROM, VALUE_TO)
        }
    }

    @Test fun notThrowOnRollTag() {
        InputItem(InputAction.ROLL, VALUE_FROM, VALUE_TO, cursor)
    }


    @Test fun get() {
        val item = InputItem(InputAction.TEXT, VALUE_FROM, VALUE_TO, cursor)

        assertEquals(VALUE_FROM, item[true])
        assertEquals(VALUE_TO, item[false])
    }

    @Test fun cursorGet() {
        val item = InputItem(InputAction.TEXT, VALUE_FROM, VALUE_TO, cursor)

        assertEquals(CURSOR_FROM, item.cursor[true])
        assertEquals(CURSOR_TO, item.cursor[false])
    }

    @Test fun nullCursorGet() {
        val item = InputItem(InputAction.COLOR, VALUE_FROM, VALUE_TO)

        assertEquals(InputItem.Cursor.ND_VALUE, item.cursor[true])
        assertEquals(InputItem.Cursor.ND_VALUE, item.cursor[false])
    }


    private val cursor = InputItem.Cursor(CURSOR_FROM, CURSOR_TO)

    companion object {
        const val VALUE_FROM = "12345"
        const val VALUE_TO = "54321"

        const val CURSOR_FROM = 1
        const val CURSOR_TO = 4
    }

}