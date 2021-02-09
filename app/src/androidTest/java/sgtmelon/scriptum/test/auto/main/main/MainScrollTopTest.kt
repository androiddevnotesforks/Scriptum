package sgtmelon.scriptum.test.auto.main.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.ui.ParentRecyclerItem

/**
 * Test add dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainScrollTopTest : ParentUiTest() {

    @Test fun onRank() = data.fillRank(count = 20).let {
        launch {
            mainScreen {
                rankScreen { onScroll(Scroll.END) }.onScrollTop()
                ParentRecyclerItem.PREVENT_SCROLL = true
                rankScreen { openRenameDialog(it.first().name, p = 0) { onCloseSoft() } }
            }
        }
    }

    @Test fun onNotes() = data.fillNotes().let {
        launch {
            mainScreen {
                notesScreen { onScroll(Scroll.END) }.onScrollTop()
                ParentRecyclerItem.PREVENT_SCROLL = true
                notesScreen { onAssertItem(it.first(), p = 0) }
            }
        }
    }

    @Test fun onBin() = data.fillBin().let {
        launch {
            mainScreen {
                binScreen { onScroll(Scroll.END) }.onScrollTop()
                ParentRecyclerItem.PREVENT_SCROLL = true
                binScreen { onAssertItem(it.first(), p = 0) }
            }
        }
    }

}