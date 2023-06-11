package sgtmelon.scriptum.cleanup.test.ui.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

import sgtmelon.scriptum.source.ui.model.key.Scroll
import sgtmelon.scriptum.source.ui.tests.ParentUiWeighTest

/**
 * Test recyclerView lists for lags
 */
@RunWith(AndroidJUnit4::class)
class ListTest : ParentUiWeighTest() {

    // TODO #TEST optimization textNote inside lists (because now I load all text length)

    @Test fun rollNoteScroll() = db.insertRoll(
        isVisible = true,
        list = dbWeight.rollList
    ).let {
        launchSplash {
            mainScreen { openNotes { openRoll(it) { scrollTo(Scroll.END, SCROLL_COUNT) } } }
        }
    }

    companion object {
        private const val NOTES_COUNT = 250
    }
}