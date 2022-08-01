package sgtmelon.scriptum.cleanup.data.room.entity

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Alarm.Default
import sgtmelon.scriptum.cleanup.parent.ParentTest


/**
 * Test for [AlarmEntity].
 */
class AlarmEntityTest : ParentTest() {

    @Test fun defaultValues() {
        with(AlarmEntity()) {
            assertEquals(Default.ID, id)
            assertEquals(Default.NOTE_ID, noteId)
            assertEquals(Default.DATE, date)
        }
    }
}