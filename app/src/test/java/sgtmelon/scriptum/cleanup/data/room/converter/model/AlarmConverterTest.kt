package sgtmelon.scriptum.cleanup.data.room.converter.model

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteAlarm
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Test for [AlarmConverter].
 */
class AlarmConverterTest : sgtmelon.tests.uniter.ParentTest() {

    //region Data

    private val noteAlarm = NoteAlarm(id = 5, date = "12345")
    private val item = NoteItem.Text(id = 1, color = Color.values().random(), alarm = noteAlarm)
    private val entity = AlarmEntity(id = noteAlarm.id, noteId = 1, date = noteAlarm.date)

    //endregion

    private val converter = AlarmConverter()

    @Test fun toEntity() = assertEquals(converter.toEntity(item), entity)

    @Test fun toNoteAlarm() = assertEquals(converter.toNoteAlarm(entity), noteAlarm)
}