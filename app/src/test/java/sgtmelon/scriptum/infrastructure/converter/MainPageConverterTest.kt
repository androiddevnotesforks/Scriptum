package sgtmelon.scriptum.infrastructure.converter

import android.view.MenuItem
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.infrastructure.model.exception.converter.ConverterException
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.utils.extensions.record

/**
 * Test for [MainPageConverter].
 */
class MainPageConverterTest {

    private val converter = MainPageConverter()

    @Test fun `mainPage to id`() {
        val pageArray = MainPage.values()
        val idList = listOf(R.id.item_page_rank, R.id.item_page_notes, R.id.item_page_bin)

        assertEquals(idList.size, pageArray.size)

        for ((i, page) in pageArray.withIndex()) {
            assertEquals(converter.convert(page), idList[i])
        }
    }

    @Test fun `mainPage to id with exception`() {
        FastMock.fireExtensions()
        every { any<ConverterException>().record() } returns mockk()

        assertEquals(converter.convert(page = null), MainPageConverter.INVALID_ID)
    }

    @Test fun `menuItem to page`() {
        val idList = listOf(R.id.item_page_rank, R.id.item_page_notes, R.id.item_page_bin)
        val itemList = List(idList.size) { mockk<MenuItem>() }
        val pageArray = MainPage.values()

        for ((i, item) in itemList.withIndex()) {
            every { item.itemId } returns idList[i]
        }

        assertEquals(idList.size, pageArray.size)

        for ((i, item) in itemList.withIndex()) {
            assertEquals(converter.convert(item), pageArray[i])
        }

        verifySequence {
            for (item in itemList) {
                item.itemId
            }
        }
    }

    @Test fun `menuItem to page with exception`() {
        val menuItem = mockk<MenuItem>()
        val id = -1

        every { menuItem.itemId } returns id
        FastMock.fireExtensions()
        every { any<ConverterException>().record() } returns mockk()

        assertNull(converter.convert(menuItem))

        verifySequence {
            menuItem.itemId
        }
    }
}