package sgtmelon.scriptum.cleanup.data.room.entity

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.infrastructure.database.DbData.Roll.Default

/**
 * Test for [RollEntity].
 */
class RollEntityTest : sgtmelon.tests.uniter.ParentTest() {

    @Test fun defaultValues() {
        with(RollEntity()) {
            assertEquals(Default.ID, id)
            assertEquals(Default.NOTE_ID, noteId)
            assertEquals(Default.POSITION, position)
            assertEquals(Default.CHECK, isCheck)
            assertEquals(Default.TEXT, text)
        }
    }
}