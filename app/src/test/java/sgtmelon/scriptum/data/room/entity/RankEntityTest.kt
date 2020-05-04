package sgtmelon.scriptum.data.room.entity

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.domain.model.data.DbData.Rank.Default

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