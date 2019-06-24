package sgtmelon.scriptum.test.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.view.main.MainActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест работы [MainActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class MainTest : ParentUiTest() {

    private val pageList = arrayListOf(
            MainPage.RANK, MainPage.NOTES, MainPage.BIN,
            MainPage.RANK, MainPage.BIN, MainPage.NOTES
    )

    @Test fun startScreen() = launch { mainScreen() }

    @Test fun menuClickCorrectScreen() = launch {
        mainScreen {
            repeat(times = 3) {
                pageList.forEach {
                    when (it) {
                        MainPage.RANK -> openRankPage(empty = true)
                        MainPage.NOTES -> openNotesPage(empty = true)
                        MainPage.BIN -> openBinPage(empty = true)
                    }
                }
            }
        }
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

    @Test fun addDialogCreateTextNote() = testData.createText().let {
        launch { mainScreen { openAddDialog { createTextNote(it) } } }
    }

    @Test fun addDialogCreateRollNote() = testData.createRoll().let {
        launch { mainScreen { openAddDialog { createRollNote(it) } } }
    }

    /**
     * Page Scroll Top
     */

    @Test fun rankScreenScrollTop() = launch({ testData.fillRank() }) {
        mainScreen {
            openRankPage { onScroll(Scroll.END) }
            onScrollTop(MainPage.RANK)
        }
    }

    @Test fun notesScreenScrollTop() = launch({ testData.fillNotes() }) {
        mainScreen {
            openNotesPage { onScroll(Scroll.END) }
            onScrollTop(MainPage.NOTES)
        }
    }

    @Test fun binScreenScrollTop() = launch({ testData.fillBin() }) {
        mainScreen {
            openBinPage { onScroll(Scroll.END) }
            onScrollTop(MainPage.BIN)
        }
    }

}