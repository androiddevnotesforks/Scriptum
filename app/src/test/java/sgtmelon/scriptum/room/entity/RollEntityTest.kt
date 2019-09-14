package sgtmelon.scriptum.room.entity

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test for [RollEntity]
 */
class RollEntityTest {

    @Test fun toJSON() {
        assertEquals(stringFirst, rollFirst.toString())
    }

    @Test fun fromJSON() {
        assertEquals(rollFirst, RollEntity[stringFirst])
        assertEquals(rollSecond, RollEntity[stringSecond])
    }

    private companion object {
        val rollFirst = RollEntity()
        const val stringFirst = """{"RL_POSITION":0,"RL_TEXT":"","RL_ID":-1,"RL_CHECK":false,"RL_NOTE_ID":0}"""

        val rollSecond = null
        const val stringSecond = "Incorrect string"
    }

}