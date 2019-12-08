package sgtmelon.scriptum.room.converter.model

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.room.entity.AlarmEntity

/**
 * Test for [AlarmConverter]
 */
class AlarmConverterTest {

    private val converter = AlarmConverter()
    
    @Test fun toEntity() = assertEquals(entity, converter.toEntity(item))
    
    private companion object {
        val item = NoteItem(
                id = 10, create = "123", color = 0, type = NoteType.TEXT,
                alarmId = 5, alarmDate = "12345"
        )

        val entity = AlarmEntity(id = 5, noteId = 10, date = "12345")
    }

}