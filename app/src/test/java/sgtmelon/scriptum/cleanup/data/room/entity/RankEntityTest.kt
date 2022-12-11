package sgtmelon.scriptum.cleanup.data.room.entity

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.infrastructure.database.DbData.Rank.Default
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Test for [RankEntity].
 */
class RankEntityTest : ParentTest() {

    @Test fun defaultValues() {
        with(RankEntity()) {
            assertEquals(Default.ID,id)
            assertEquals(Default.NOTE_ID,noteId)
            assertEquals(Default.POSITION,position)
            assertEquals(Default.NAME,name)
            assertEquals(Default.VISIBLE, isVisible)
        }
    }
}