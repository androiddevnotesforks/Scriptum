package sgtmelon.scriptum.data.room.converter.model

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.data.room.entity.RankEntity
import sgtmelon.scriptum.domain.model.item.RankItem

/**
 * Test for [RankConverter].
 */
class RankConverterTest : ParentTest() {

    private val converter = RankConverter()

    @Test fun toItem() {
        TODO()
        assertEquals(itemFirst, converter.toItem(entityFirst))
        assertEquals(itemSecond, converter.toItem(entitySecond))

        assertEquals(itemList, converter.toItem(entityList))
    }

    @Test fun toEntity() {
        TODO()
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