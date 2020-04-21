package sgtmelon.scriptum.data.room.converter.model

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.data.room.entity.AlarmEntity
import sgtmelon.scriptum.data.room.entity.NoteEntity
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.key.NoteType

/**
 * Test for [NoteConverter].
 */
class NoteConverterTest : ParentTest() {

    private val converter = NoteConverter()

    @Test fun toItem() {
        assertEquals(noteItem, converter.toItem(noteEntity, rollList, alarmEntity))
    }

    @Test fun toEntity() {
        assertEquals(noteEntity, converter.toEntity(noteItem))
    }

    private companion object {
        val noteEntity = NoteEntity(
                id = 1, create = "12", change = "34", name = "bla", text = "bla", color = 1,
                type = NoteType.ROLL, rankId = 1, rankPs = 1, isBin = true, isStatus = true
        )

        val rollList = mutableListOf(
                RollItem(id = 1, position = 0, text = "123"),
                RollItem(id = 2, position = 1, text = "234", isCheck = true),
                RollItem(id = 3, position = 2, text = "345"),
                RollItem(id = 4, position = 3, text = "456", isCheck = true)
        )

        val alarmEntity = AlarmEntity(id = 1, noteId = 1, date = "12345")

        val noteItem = NoteItem.Roll(
                id = 1, create = "12", change = "34", name = "bla", text = "bla", color = 1,
                rankId = 1, rankPs = 1, isBin = true, isStatus = true,
                alarmId = 1, alarmDate = "12345", rollList = rollList
        )
    }

}