package sgtmelon.scriptum.room.converter.model

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.room.entity.RollEntity

/**
 * Test for [RollConverter].
 */
class RollConverterTest : ParentTest() {

    private val converter = RollConverter()

    @Test fun toItem() {
        assertEquals(itemFirst, converter.toItem(entityFirst))
        assertEquals(itemSecond, converter.toItem(entitySecond))

        val itemList = mutableListOf(itemFirst, itemSecond)
        val entityList = mutableListOf(entityFirst, entitySecond)

        assertEquals(itemList, converter.toItem(entityList))
    }

    @Test fun toEntity() {
        assertEquals(entityFirst, converter.toEntity(noteId, itemFirst))
        assertEquals(entitySecond, converter.toEntity(noteId, itemSecond))
    }

    private companion object {
        const val noteId = 1L

        val entityFirst = RollEntity(id = 1, noteId = noteId, position = 0, isCheck = true, text = "123")
        val entitySecond = RollEntity(id = 2, noteId = noteId, position = 1, text = "234")

        val itemFirst = RollItem(id = 1, position = 0, isCheck = true, text = "123")
        val itemSecond = RollItem(id = 2, position = 1, text = "234")
    }

}