package sgtmelon.scriptum.cleanup.data.room.converter.model

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.model.key.Color

/**
 * Test for [AlarmConverter].
 */
class AlarmConverterTest : ParentTest() {

    //region Data

    private val item = NoteItem.Text(id = 1, create = nextString(), color = Color.values().random(), alarmId = 5, alarmDate = "12345")
    private val entity = AlarmEntity(id = 5, noteId = 1, date = "12345")

    //endregion

    private val converter = AlarmConverter()

    @Test fun toEntity() = assertEquals(entity, converter.toEntity(item))

}