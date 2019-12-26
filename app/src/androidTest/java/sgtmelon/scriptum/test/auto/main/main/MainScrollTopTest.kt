package sgtmelon.scriptum.test.auto.main.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.ParentRecyclerItem.Companion.PREVENT_SCROLL

/**
 * Test add dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainScrollTopTest : ParentUiTest() {

    @Test fun onRank() = data.fillRank().let {
        launch {
            mainScreen {
                rankScreen { onScroll(Scroll.END) }.onScrollTop()
                PREVENT_SCROLL = true
                rankScreen { openRenameDialog(it.first().name, p = 0) }
            }
        }
    }

    @Test fun onNotes() = data.fillNotes().let {
        launch {
            mainScreen {
                notesScreen { onScroll(Scroll.END) }.onScrollTop()
                PREVENT_SCROLL = true
                notesScreen { openNoteDialog(it.first(), p = 0) }
            }
        }
    }

    @Test fun onBin() = data.fillBin().let {
        launch {
            mainScreen {
                binScreen { onScroll(Scroll.END) }.onScrollTop()
                PREVENT_SCROLL = true
                binScreen { openNoteDialog(it.first(), p = 0) }
            }
        }
    }

}