package sgtmelon.scriptum.cleanup.data.room.converter.model

import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.parent.ParentTest

/**
 * Test for [RankConverter].
 */
class RankConverterTest : ParentTest() {

    //region Data

    private val firstEntity = RankEntity(id = 1, noteId = mutableListOf(1, 2), position = 0, name = "123", isVisible = false)
    private val secondEntity = RankEntity(id = 2, noteId = mutableListOf(2), position = 1, name = "234")

    private val firstItem = RankItem(id = 1, noteId = mutableListOf(1, 2), position = 0, name = "123", isVisible = false)
    private val secondItem = RankItem(id = 2, noteId = mutableListOf(2), position = 1, name = "234")

    private val itemList = mutableListOf(firstItem, secondItem)
    private val entityList = mutableListOf(firstEntity, secondEntity)

    //endregion

    private val converter = RankConverter()

    @Test fun toItem() {
        assertEquals(firstItem, converter.toItem(firstEntity))
        assertEquals(secondItem, converter.toItem(secondEntity))

        assertEquals(itemList, converter.toItem(entityList))
    }

    @Test fun toEntity() {
        assertEquals(firstEntity, converter.toEntity(firstItem))
        assertEquals(secondEntity, converter.toEntity(secondItem))

        assertEquals(entityList, converter.toEntity(itemList))
    }
}