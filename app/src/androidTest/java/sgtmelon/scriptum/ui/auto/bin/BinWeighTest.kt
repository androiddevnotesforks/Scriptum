package sgtmelon.scriptum.ui.auto.bin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.bin.BinFragment
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.tests.ParentUiWeighTest

/**
 * Weigh test for [BinFragment].
 */
@RunWith(AndroidJUnit4::class)
class BinWeighTest : ParentUiWeighTest() {

    @Test fun binScroll() = launch({ db.fillBin(ITEM_COUNT) }) {
        mainScreen { openBin { scrollTo(Scroll.END, SCROLL_COUNT) } }
    }

    /**
     * Heavy = 30.223s
     * Simple = 19.780s
     * Coroutine = 21.930s
     */
    @Test fun textNoteOpen() = db.insertText(dbWeight.textNote).let { model ->
        launch {
            mainScreen {
                openNotes {
                    repeat(REPEAT_COUNT) { openText(model) { toolbar { clickBack() } } }
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
                openNotes {
                    repeat(REPEAT_COUNT) { openRoll(model) { toolbar { clickBack() } } }
                }
            }
        }
    }

}