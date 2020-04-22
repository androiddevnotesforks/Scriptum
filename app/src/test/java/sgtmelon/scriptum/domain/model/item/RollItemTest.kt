package sgtmelon.scriptum.domain.model.item

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentTest

/**
 * Test for [RollItem].
 */
class RollItemTest : ParentTest() {

    @Test fun getJson() {
            TODO()

        assertEquals(itemFirst.toJson(), stringFirst)
        assertEquals(itemSecond.toJson(), stringSecond)
    }

    @Test fun getItem() {
            TODO()

        assertEquals(itemFirst, RollItem[stringFirst])
        assertEquals(itemSecond, RollItem[stringSecond])

        assertNull(RollItem[wrongString])
    }

    companion object {
        val itemFirst = RollItem(id = 10, position = 5, isCheck = true, text = "12345")
        const val stringFirst = """{"RL_POSITION":5,"RL_TEXT":"12345","RL_ID":10,"RL_CHECK":true}"""

        val itemSecond = RollItem(position = 5, isCheck = true, text = "12345")
        const val stringSecond = """{"RL_POSITION":5,"RL_TEXT":"12345","RL_ID":-1,"RL_CHECK":true}"""

        const val wrongString = "TEST_WRONG_STRING"
    }

}