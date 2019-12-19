package sgtmelon.scriptum.test.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.ParentRecyclerItem.Companion.PREVENT_SCROLL

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

    @Test fun addDialogUse() = launch {
        mainScreen {
            openAddDialog { onCloseSoft() }.assert()
            openAddDialog { onCloseSwipe() }.assert()
        }
    }

    @Test fun addDialogCreateTextNote() = data.createText().let {
        launch { mainScreen { openAddDialog { createText(it) } } }
    }

    @Test fun addDialogCreateRollNote() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRoll(it) } } }
    }

    /**
     * Page Scroll Top
     */

    @Test fun rankScreenScrollTop() = data.fillRank().let {
        launch {
            mainScreen {
                rankScreen { onScroll(Scroll.END) }.onScrollTop()
                PREVENT_SCROLL = true
                rankScreen { openRenameDialog(it.first().name, p = 0) }
            }
        }
    }

    @Test fun notesScreenScrollTop() = data.fillNotes().let {
        launch {
            mainScreen {
                notesScreen { onScroll(Scroll.END) }.onScrollTop()
                PREVENT_SCROLL = true
                notesScreen { openNoteDialog(it.first(), p = 0) }
            }
        }
    }

    @Test fun binScreenScrollTop() = data.fillBin().let {
        launch {
            mainScreen {
                binScreen { onScroll(Scroll.END) }.onScrollTop()
                PREVENT_SCROLL = true
                binScreen { openNoteDialog(it.first(), p = 0) }
            }
        }
    }

    private companion object {
        val pageList = arrayListOf(
                MainPage.RANK, MainPage.NOTES, MainPage.BIN,
                MainPage.RANK, MainPage.BIN, MainPage.NOTES
        )
    }

}