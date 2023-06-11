package sgtmelon.scriptum.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.source.ui.model.key.Scroll
import sgtmelon.scriptum.source.ui.parts.recycler.RecyclerItemPart
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchMain

/**
 * Test for [MainActivity], scrolling page top on double tab click.
 */
@RunWith(AndroidJUnit4::class)
class MainScrollTopTest : ParentUiTest() {

    @Test fun onRank() = db.fillRank(ITEM_COUNT).let {
        launchMain {
            openRank { scrollTo(Scroll.END) }
            scrollTop()
            RecyclerItemPart.PREVENT_SCROLL = true
            openRank { assertItem(it.first(), p = 0) }
        }
    }

    @Test fun onNotes() = db.fillNotes(ITEM_COUNT).let {
        launchMain {
            openNotes { scrollTo(Scroll.END) }
            scrollTop()
            RecyclerItemPart.PREVENT_SCROLL = true
            openNotes { assertItem(it.first(), p = 0) }
        }
    }

    @Test fun onBin() = db.fillBin(ITEM_COUNT).let {
        launchMain {
            openBin { scrollTo(Scroll.END) }
            scrollTop()
            RecyclerItemPart.PREVENT_SCROLL = true
            openBin { assertItem(it.first(), p = 0) }
        }
    }

    companion object {
        private const val ITEM_COUNT = 30
    }
}