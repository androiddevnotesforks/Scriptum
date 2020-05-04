package sgtmelon.scriptum.domain.model.item

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.domain.model.data.DbData.Roll.Default
import kotlin.random.Random

/**
 * Test for [RollItem].
 */
class RollItemTest : ParentTest() {

    @Test fun defaultValues() {
        val item = RollItem(position = Random.nextInt(), text = Random.nextInt().toString())

        assertEquals(Default.ID, item.id)
        assertEquals(Default.CHECK, item.isCheck)
    }

    @Test fun getJson() {
        assertEquals(firstItem.toJson(), firstString)
        assertEquals(secondItem.toJson(), secondString)
    }

    @Test fun getItem() {
        assertEquals(firstItem, RollItem[firstString])
        assertEquals(secondItem, RollItem[secondString])

        assertNull(RollItem[wrongString])
    }


    private val firstItem = RollItem(id = 10, position = 5, isCheck = true, text = "12345")
    private val secondItem = RollItem(position = 5, isCheck = true, text = "12345")

    companion object {
        private const val firstString = """{"RL_POSITION":5,"RL_TEXT":"12345","RL_ID":10,"RL_CHECK":true}"""
        private const val secondString = """{"RL_POSITION":5,"RL_TEXT":"12345","RL_ID":-1,"RL_CHECK":true}"""
        private const val wrongString = "TEST_WRONG_STRING"
    }

}