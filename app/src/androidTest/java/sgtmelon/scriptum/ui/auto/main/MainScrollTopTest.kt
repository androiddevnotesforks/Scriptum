package sgtmelon.scriptum.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test for [MainActivity], scrolling page top on double tab click.
 */
@RunWith(AndroidJUnit4::class)
class MainScrollTopTest : ParentUiTest() {

    @Test fun onRank() = db.fillRank(count = 20).let {
        launch {
            mainScreen {
                rankScreen { onScroll(Scroll.END) }.onScrollTop()
                RecyclerItemPart.PREVENT_SCROLL = true
                rankScreen { onAssertItem(it.first(), p = 0) }
            }
        }
    }

    @Test fun onNotes() = db.fillNotes().let {
        launch {
            mainScreen {
                notesScreen { onScroll(Scroll.END) }.onScrollTop()
                RecyclerItemPart.PREVENT_SCROLL = true
                notesScreen { onAssertItem(it.first(), p = 0) }
            }
        }
    }

    @Test fun onBin() = db.fillBin().let {
        launch {
            mainScreen {
                binScreen { onScroll(Scroll.END) }.onScrollTop()
                RecyclerItemPart.PREVENT_SCROLL = true
                binScreen { onAssertItem(it.first(), p = 0) }
            }
        }
    }
}