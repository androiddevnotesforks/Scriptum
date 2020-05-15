package sgtmelon.scriptum.data.room.converter.model

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.data.room.entity.RollEntity
import sgtmelon.scriptum.domain.model.item.RollItem

/**
 * Test for [RollConverter].
 */
class RollConverterTest : ParentTest() {

    private val converter = RollConverter()

    @Test fun toItem() {
        assertEquals(firstItem, converter.toItem(firstEntity))
        assertEquals(secondItem, converter.toItem(secondEntity))

        val itemList = mutableListOf(firstItem, secondItem)
        val entityList = mutableListOf(firstEntity, secondEntity)

        assertEquals(itemList, converter.toItem(entityList))
    }

    @Test fun toEntity() {
        assertEquals(firstEntity, converter.toEntity(noteId, firstItem))
        assertEquals(secondEntity, converter.toEntity(noteId, secondItem))

        val itemList = mutableListOf(firstItem, secondItem)
        val entityList = mutableListOf(firstEntity, secondEntity)

        assertEquals(entityList, converter.toEntity(noteId, itemList))
    }

    private val firstEntity = RollEntity(id = 1, noteId = noteId, position = 0, isCheck = true, text = "123")
    private val secondEntity = RollEntity(id = 2, noteId = noteId, position = 1, text = "234")

    private val firstItem = RollItem(id = 1, position = 0, isCheck = true, text = "123")
    private val secondItem = RollItem(id = 2, position = 1, text = "234")

    companion object {
        private const val noteId = 1L
    }

}