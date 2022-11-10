package sgtmelon.scriptum.cleanup.test.ui.weight

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.tests.ParentUiWeighTest

/**
 * Test recyclerView lists for lags
 */
@RunWith(AndroidJUnit4::class)
class ListTest : ParentUiWeighTest() {

    // TODO #TEST optimization textNote inside lists (because now I load all text length)

    @Test fun rankScroll() = launch({ db.fillRank(RANK_COUNT) }) {
        mainScreen { rankScreen { onScroll(Scroll.END, SCROLL_COUNT) } }
    }

    @Test fun notesScroll() = launch({ db.fillNotes(NOTES_COUNT) }) {
        mainScreen { notesScreen { onScroll(Scroll.END, SCROLL_COUNT) } }
    }

    @Test fun binScroll() = launch({ db.fillBin(BIN_COUNT) }) {
        mainScreen { binScreen { onScroll(Scroll.END, SCROLL_COUNT) } }
    }


    /**
     * Heavy = 30.223s
     * Simple = 19.780s
     * Coroutine = 21.930s
     */
    @Test fun textNoteOpen() = db.insertText(dbWeight.textNote).let { model ->
        launch {
            mainScreen {
                notesScreen {
                    repeat(REPEAT_COUNT) { openTextNote(model) { toolbar { clickBack() } } }
                }
            }
        }
    }

    @Test fun rollNoteOpen() = db.insertRoll(
        isVisible = true,
        list = dbWeight.rollList
    ).let { model ->
        launch {
            mainScreen {
                notesScreen {
                    repeat(REPEAT_COUNT) { openRollNote(model) { toolbar { clickBack() } } }
                }
            }
        }
    }

    @Test fun rollNoteScroll() = db.insertRoll(
        isVisible = true,
        list = dbWeight.rollList
    ).let {
        launch {
            mainScreen { notesScreen { openRollNote(it) { onScroll(Scroll.END, SCROLL_COUNT) } } }
        }
    }

    companion object {
        private const val RANK_COUNT = 200
        private const val NOTES_COUNT = 250
        private const val BIN_COUNT = 250
    }
}