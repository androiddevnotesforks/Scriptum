package sgtmelon.scriptum.test.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [MainActivity]
 */
@RunWith(AndroidJUnit4::class)
class MainTest : ParentUiTest() {

    @Test fun startScreen() = launch { mainScreen() }

    @Test fun menuClickCorrectScreen() = launch {
        mainScreen { repeat(times = 3) { pageList.forEach { openPage(it, empty = true) } } }
    }

    @Test fun addFabVisible() = launch {
        mainScreen { repeat(times = 3) { pageList.forEach { onNavigateTo(it) } } }
    }

    /**
     * Add Dialog
     */

    @Test fun addDialogOpen() = launch { mainScreen { addDialog() } }

    @Test fun addDialogClose() = launch {
        mainScreen {
            addDialog { onCloseSoft() }.assert()
            addDialog { onCloseSwipe() }.assert()
        }
    }

    @Test fun addDialogCreateTextNote() = data.createText().let {
        launch { mainScreen { addDialog { createText(it) } } }
    }

    @Test fun addDialogCreateRollNote() = data.createRoll().let {
        launch { mainScreen { addDialog { createRoll(it) } } }
    }

    /**
     * Page Scroll Top
     */

    @Test fun rankScreenScrollTop() = launch({ data.fillRank() }) {
        mainScreen { rankScreen { onScroll(Scroll.END) }.onScrollTop() }
    }

    @Test fun notesScreenScrollTop() = launch({ data.fillNotes() }) {
        mainScreen { notesScreen { onScroll(Scroll.END) }.onScrollTop() }
    }

    @Test fun binScreenScrollTop() = launch({ data.fillBin() }) {
        mainScreen { binScreen { onScroll(Scroll.END) }.onScrollTop() }
    }


    private companion object {
        val pageList = arrayListOf(
                MainPage.RANK, MainPage.NOTES, MainPage.BIN,
                MainPage.RANK, MainPage.BIN, MainPage.NOTES
        )
    }

}