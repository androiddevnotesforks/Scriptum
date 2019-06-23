package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.waitAfter
import sgtmelon.scriptum.waitBefore

/**
 * Тест работы анимации информации о пустом списке
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class InfoAnimTest : ParentUiTest() {

    @Test fun rankShowAndHide() = testData.rankEntity.let {
        launch {
            mainScreen {
                openRankPage(empty = true) {
                    repeat(times = 3) { _ ->
                        toolbar {
                            onEnterName(it.name, isAddEnabled = true)
                            onClickAdd()
                        }

                        waitAfter(WAIT) { waitBefore(WAIT) { onClickCancel(it) } }
                    }
                }
            }
        }
    }


    @Test fun notesShow() = testData.insertText().let {
        launch {
            mainScreen {
                waitAfter(WAIT) { openNotesPage { openNoteDialog(it) { onClickDelete() } } }
            }
        }
    }

    @Test fun notesHide() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openNoteDialog(it) { onClickRestore() } }
                waitAfter(WAIT) { openNotesPage() }
            }
        }
    }


    @Test fun binShow() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                waitAfter(WAIT) { openBinPage { openNoteDialog(it) { onClickClear() } } }
            }
        }
    }

    @Test fun binHide() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openNoteDialog(it) { onClickDelete() } }
                waitAfter(WAIT) { openBinPage() }
            }
        }
    }

    @Test fun notificationShow() = testData.insertNotification(testData.insertText()).let {
        launch {
            mainScreen {
                openNotesPage { openNotification { waitAfter(WAIT) { onClickCancel(it) } } }
            }
        }
    }

    private companion object {
        const val WAIT = 500L
    }

}