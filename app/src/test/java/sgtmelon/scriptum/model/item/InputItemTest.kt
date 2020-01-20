package sgtmelon.scriptum.model.item

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.model.annotation.InputAction
import java.lang.NullPointerException
import sgtmelon.scriptum.model.item.InputItem.Cursor.Companion.get

/**
 * Test for [InputItem].
 */
class InputItemTest : ParentTest() {

    @get:Rule val exception: ExpectedException = ExpectedException.none()

    @Test fun throwOnNameTag() {
        exception.expect(NullPointerException::class.java)
        InputItem(InputAction.NAME, VALUE_FROM, VALUE_TO)
    }

    @Test fun notThrowOnNameTag() {
        InputItem(InputAction.NAME, VALUE_FROM, VALUE_TO, cursor)
    }

    @Test fun throwOnTextTag() {
        exception.expect(NullPointerException::class.java)
        InputItem(InputAction.TEXT, VALUE_FROM, VALUE_TO)
    }

    @Test fun notThrowOnTextTag() {
        InputItem(InputAction.TEXT, VALUE_FROM, VALUE_TO, cursor)
    }

    @Test fun throwOnRollTag() {
        exception.expect(NullPointerException::class.java)
        InputItem(InputAction.ROLL, VALUE_FROM, VALUE_TO)
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


    private companion object {
        const val VALUE_FROM = "12345"
        const val VALUE_TO = "54321"

        const val CURSOR_FROM = 1
        const val CURSOR_TO = 4

        val cursor = InputItem.Cursor(CURSOR_FROM, CURSOR_TO)
    }

}