package sgtmelon.scriptum.cleanup.domain.model.item

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.infrastructure.database.DbData.Rank.Default
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [RankItem].
 */
class RankItemTest : ParentTest() {

    //region Data

    private val rankItem = RankItem(id = 0, name = "item")

    private val firstItem = RankItem(
        id = 10, noteId = mutableListOf(1L, 10L, -5L), position = 5, name = "hula na",
        isVisible = true, bindCount = 123, notificationCount = 456
    )
    private val secondItem = RankItem(id = 13, position = 0, name = "hua", isVisible = false)

    private val firstString = """{"RK_VISIBLE":true,"RK_NOTIFICATION_COUNT":456,"RK_NOTE_ID":[1,10,-5],"RK_ID":10,"RK_NAME":"hula na","RK_BIND_COUNT":123,"RK_POSITION":5}"""
    private val secondString = """{"RK_VISIBLE":false,"RK_NOTIFICATION_COUNT":0,"RK_NOTE_ID":[],"RK_ID":13,"RK_NAME":"hua","RK_BIND_COUNT":0,"RK_POSITION":0}"""
    private val wrongString = nextString()

    //endregion

    @Test fun defaultValues() = with(rankItem) {
        assertEquals(Default.NOTE_ID, noteId)
        assertEquals(Default.POSITION, position)
        assertEquals(Default.VISIBLE, isVisible)

        assertEquals(Default.BIND_COUNT, bindCount)
        assertEquals(Default.NOTIFICATION_COUNT, notificationCount)
    }

    @Test fun getJson() {
        assertEquals(firstItem.toJson(), firstString)
        assertEquals(secondItem.toJson(), secondString)
    }

    @Test fun getItem() {
        assertEquals(firstItem, RankItem[firstString])
        assertEquals(secondItem, RankItem[secondString])

        assertNull(RankItem[wrongString])
    }
}