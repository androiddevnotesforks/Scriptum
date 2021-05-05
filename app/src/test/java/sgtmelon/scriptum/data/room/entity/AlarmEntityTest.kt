package sgtmelon.scriptum.data.room.entity

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.domain.model.data.DbData.Alarm.Default
import sgtmelon.scriptum.parent.ParentTest


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