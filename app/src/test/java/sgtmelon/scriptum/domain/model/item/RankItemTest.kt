package sgtmelon.scriptum.domain.model.item

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.domain.model.data.DbData.Rank.Default

/**
 * Test for [RankItem].
 */
class RankItemTest : ParentTest() {

    //region Data

    private val rankItem = RankItem(id = 0, name = "item")

    //endregion

    @Test fun defaultValues() = with(rankItem) {
        assertEquals(Default.NOTE_ID, noteId)
        assertEquals(Default.POSITION, position)
        assertEquals(Default.VISIBLE, isVisible)

        assertEquals(RankItem.ND_BIND_COUNT, bindCount)
        assertEquals(RankItem.ND_NOTIFICATION_COUNT, notificationCount)
    }

    @Test fun switchVisible() {
        assertTrue(rankItem.copy(isVisible = false).switchVisible().isVisible)
        assertFalse(rankItem.copy(isVisible = true).switchVisible().isVisible)
    }
}