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

    @Test fun onRank() = db.fillRank(ITEM_COUNT).let {
        launch {
            mainScreen {
                rankScreen { scrollTo(Scroll.END) }.scrollTop()
                RecyclerItemPart.PREVENT_SCROLL = true
                rankScreen { assertItem(it.first(), p = 0) }
            }
        }
    }

    @Test fun onNotes() = db.fillNotes(ITEM_COUNT).let {
        launch {
            mainScreen {
                notesScreen { scrollTo(Scroll.END) }.scrollTop()
                RecyclerItemPart.PREVENT_SCROLL = true
                notesScreen { assertItem(it.first(), p = 0) }
            }
        }
    }

    @Test fun onBin() = db.fillBin(ITEM_COUNT).let {
        launch {
            mainScreen {
                binScreen { scrollTo(Scroll.END) }.scrollTop()
                RecyclerItemPart.PREVENT_SCROLL = true
                binScreen { assertItem(it.first(), p = 0) }
            }
        }
    }

    companion object {
        private const val ITEM_COUNT = 30
    }
}