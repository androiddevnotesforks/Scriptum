package sgtmelon.scriptum.data.room.entity

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.domain.model.data.DbData.Note.Default

/**
 * Test for [NoteEntity].
 */
class NoteEntityTest : ParentTest() {

    @Test fun defaultValues() {
        with(NoteEntity()) {
            assertEquals(Default.ID, id)
            assertEquals(Default.CREATE, create)
            assertEquals(Default.CHANGE, change)
            assertEquals(Default.NAME, name)
            assertEquals(Default.TEXT, text)
            assertEquals(Default.COLOR, color)
            assertEquals(Default.TYPE, type)
            assertEquals(Default.RANK_ID, rankId)
            assertEquals(Default.RANK_PS, rankPs)
            assertEquals(Default.BIN, isBin)
            assertEquals(Default.STATUS, isStatus)
        }
    }

}