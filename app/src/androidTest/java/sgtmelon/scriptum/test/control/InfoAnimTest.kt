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

                        waitAfter(TIME) { waitBefore(TIME) { onClickCancel(it) } }
                    }
                }
            }
        }
    }


    @Test fun notesShow() = testData.insertText().let {
        launch {
            mainScreen {
                waitAfter(TIME) { openNotesPage { openNoteDialog(it) { onClickDelete() } } }
            }
        }
    }

    @Test fun notesHide() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage { openNoteDialog(it) { onClickRestore() } }
                waitAfter(TIME) { openNotesPage() }
            }
        }
    }


    @Test fun binShow() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                waitAfter(TIME) { openBinPage { openNoteDialog(it) { onClickClear() } } }
            }
        }
    }

    @Test fun binHide() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage { openNoteDialog(it) { onClickDelete() } }
                waitAfter(TIME) { openBinPage() }
            }
        }
    }


    @Test fun notificationShow() = testData.insertNotification(testData.insertText()).let {
        launch {
            mainScreen {
                openNotesPage { openNotification { waitAfter(TIME) { onClickCancel(it) } } }
            }
        }
    }


    private companion object {
        const val TIME = 500L
    }

}