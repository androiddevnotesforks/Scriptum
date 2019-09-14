package sgtmelon.scriptum.test.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for[MainActivity]
 */
@RunWith(AndroidJUnit4::class)
class MainTest : ParentUiTest() {

    @Test fun startScreen() = launch { mainScreen() }

    @Test fun menuClickCorrectScreen() = launch {
        mainScreen { repeat(times = 3) { pageList.forEach { openPage(it, empty = true) } } }
    }

    @Test fun addFabVisible() =
            launch { mainScreen { repeat(times = 3) { pageList.forEach { onNavigateTo(it) } } } }

    /**
     * Add Dialog
     */

    @Test fun addDialogOpen() = launch { mainScreen { openAddDialog() } }

    @Test fun addDialogCloseSoft() = launch {
        mainScreen {
            openAddDialog { onCloseSoft() }
            assert()
        }
    }

    @Test fun addDialogCloseSwipe() = launch {
        mainScreen {
            openAddDialog { onCloseSwipe() }
            assert()
        }
    }

    @Test fun addDialogCreateTextNote() = data.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    @Test fun addDialogCreateRollNote() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRollNote(it) } } }
    }

    /**
     * Page Scroll Top
     */

    @Test fun rankScreenScrollTop() = launch({ data.fillRank() }) {
        mainScreen {
            openRankPage { onScroll(Scroll.END) }
            onScrollTop(MainPage.RANK)
        }
    }

    @Test fun notesScreenScrollTop() = launch({ data.fillNotes() }) {
        mainScreen {
            openNotesPage { onScroll(Scroll.END) }
            onScrollTop(MainPage.NOTES)
        }
    }

    @Test fun binScreenScrollTop() = launch({ data.fillBin() }) {
        mainScreen {
            openBinPage { onScroll(Scroll.END) }
            onScrollTop(MainPage.BIN)
        }
    }


    private companion object {
        val pageList = arrayListOf(
                MainPage.RANK, MainPage.NOTES, MainPage.BIN,
                MainPage.RANK, MainPage.BIN, MainPage.NOTES
        )
    }

}