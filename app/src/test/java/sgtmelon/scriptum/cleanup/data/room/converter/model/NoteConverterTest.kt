package sgtmelon.scriptum.cleanup.data.room.converter.model

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.scriptum.parent.ParentTest

/**
 * Test for [NoteConverter].
 */
class NoteConverterTest : ParentTest() {

    //region Data

    private val textEntity = NoteEntity(
        id = 1, create = "12", change = "34", name = "bla", text = "bla", color = Color.PURPLE,
        type = NoteType.TEXT, rankId = 1, rankPs = 1, isBin = true, isStatus = true
    )

    private val rollEntity = NoteEntity(
        id = 1, create = "12", change = "34", name = "bla", text = "bla", color = Color.PURPLE,
        type = NoteType.ROLL, rankId = 1, rankPs = 1, isBin = true, isStatus = true
    )

    private val rollList = mutableListOf(
        RollItem(id = 1, position = 0, text = "123"),
        RollItem(id = 2, position = 1, text = "234", isCheck = true),
        RollItem(id = 3, position = 2, text = "345"),
        RollItem(id = 4, position = 3, text = "456", isCheck = true)
    )

    private val alarmEntity = AlarmEntity(id = 1, noteId = 1, date = "12345")

    private val firstItem = NoteItem.Text(
        id = 1, create = "12", change = "34", name = "bla", text = "bla", color = Color.PURPLE,
        rankId = 1, rankPs = 1, isBin = true, isStatus = true,
        alarmId = 1, alarmDate = "12345"
    )

    private val secondItem = NoteItem.Text(
        id = 1, create = "12", change = "34", name = "bla", text = "bla", color = Color.PURPLE,
        rankId = 1, rankPs = 1, isBin = true, isStatus = true
    )

    private val thirdItem = NoteItem.Roll(
        id = 1, create = "12", change = "34", name = "bla", text = "bla", color = Color.PURPLE,
        rankId = 1, rankPs = 1, isBin = true, isStatus = true, alarmId = 1, alarmDate = "12345",
        isVisible = RollVisible.Default.VALUE, list = rollList
    )

    private val fourthItem = NoteItem.Roll(
        id = 1, create = "12", change = "34", name = "bla", text = "bla", color = Color.PURPLE,
        rankId = 1, rankPs = 1, isBin = true, isStatus = true,
        isVisible = Random.nextBoolean(), list = rollList
    )

    //endregion

    private val converter = NoteConverter()

    @Test fun toItem() {
        assertEquals(firstItem, converter.toItem(textEntity, alarmEntity = alarmEntity))
        assertEquals(secondItem, converter.toItem(textEntity))

        assertEquals(thirdItem, converter.toItem(rollEntity, null, rollList, alarmEntity))
        assertEquals(fourthItem, converter.toItem(rollEntity, fourthItem.isVisible, rollList))
    }

    @Test fun toEntity() {
        assertEquals(textEntity, converter.toEntity(firstItem))
        assertEquals(textEntity, converter.toEntity(secondItem))

        assertEquals(rollEntity, converter.toEntity(thirdItem))
        assertEquals(rollEntity, converter.toEntity(fourthItem))

        val itemList = listOf(firstItem, secondItem, thirdItem, fourthItem)
        val entityList = listOf(textEntity, textEntity, rollEntity, rollEntity)

        assertEquals(entityList, converter.toEntity(itemList))
    }
}