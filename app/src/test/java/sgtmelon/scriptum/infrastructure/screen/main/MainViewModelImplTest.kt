package sgtmelon.scriptum.infrastructure.screen.main

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.testing.getOrAwaitValue
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest

/**
 * Test for [MainViewModelImpl].
 */
class MainViewModelImplTest : ParentLiveDataTest() {

    private val viewModel by lazy { MainViewModelImpl(TODO()) }

    @Before override fun setUp() {
        super.setUp()

        assertNull(viewModel.previousPage)
        assertEquals(viewModel.currentPage.getOrAwaitValue(), MainPage.NOTES)
        assertTrue(viewModel.isFabPage)
    }

    @Test fun todo() {
        TODO()
    }

    @Test fun changePage() {
        viewModel.changePage(MainPage.RANK)
        assertEquals(viewModel.previousPage, MainPage.NOTES)
        assertEquals(viewModel.currentPage.getOrAwaitValue(), MainPage.RANK)
        assertFalse(viewModel.isFabPage)

        viewModel.changePage(MainPage.NOTES)
        assertEquals(viewModel.previousPage, MainPage.RANK)
        assertEquals(viewModel.currentPage.getOrAwaitValue(), MainPage.NOTES)
        assertTrue(viewModel.isFabPage)

        viewModel.changePage(MainPage.BIN)
        assertEquals(viewModel.previousPage, MainPage.NOTES)
        assertEquals(viewModel.currentPage.getOrAwaitValue(), MainPage.BIN)
        assertFalse(viewModel.isFabPage)
    }
}