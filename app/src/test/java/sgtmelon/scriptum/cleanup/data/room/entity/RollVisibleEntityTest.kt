package sgtmelon.scriptum.cleanup.data.room.entity

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.infrastructure.database.DbData.RollVisible.Default
import sgtmelon.tests.uniter.ParentTest

/**
 * Test for [RollVisibleEntity].
 */
class RollVisibleEntityTest : ParentTest() {

    @Test fun defaultValues() {
        with(RollVisibleEntity()) {
            assertEquals(Default.ID, id)
            assertEquals(Default.NOTE_ID, noteId)
            assertEquals(Default.VALUE, value)
        }
    }
}