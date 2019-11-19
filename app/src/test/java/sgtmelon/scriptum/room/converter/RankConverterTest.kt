package sgtmelon.scriptum.room.converter

import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.model.item.RankItem
import sgtmelon.scriptum.room.entity.RankEntity

/**
 * Test for [RankConverter]
 */
class RankConverterTest {

    private val converter = RankConverter()

    @Test fun toItem() {
        assertEquals(itemFirst, converter.toItem(entityFirst))
        assertEquals(itemSecond, converter.toItem(entitySecond))

        assertEquals(itemList, converter.toItem(entityList))
    }

    @Test fun toEntity() {
        assertEquals(entityFirst, converter.toEntity(itemFirst))
        assertEquals(entitySecond, converter.toEntity(itemSecond))

        assertEquals(entityList, converter.toEntity(itemList))
    }

    private companion object {
        val entityFirst = RankEntity(id = 1, noteId = mutableListOf(1, 2), position = 0, name = "123", isVisible = false)
        val entitySecond = RankEntity(id = 2, noteId = mutableListOf(2), position = 1, name = "234")

        val itemFirst = RankItem(id = 1, noteId = mutableListOf(1, 2), position = 0, name = "123", isVisible = false)
        val itemSecond = RankItem(id = 2, noteId = mutableListOf(2), position = 1, name = "234")

        val itemList = mutableListOf(itemFirst, itemSecond)
        val entityList = mutableListOf(entityFirst, entitySecond)
    }

}