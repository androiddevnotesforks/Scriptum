package sgtmelon.scriptum.test.control

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentTest

/**
 * Тест работы анимации информации о пустом списке
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class InfoAnimTest : ParentTest() {

    @Test fun rankShowAndHide() = launch({ testData.clear() }) {
        mainScreen {
            openRankPage {
                val name = testData.uniqueString

                repeat(times = 3) {
                    toolbar {
                        onEnterName(name)
                        closeSoftKeyboard()
                        onClickAdd()
                    }

                    wait(time = 300)
                    onClickCancel(name)
                    wait(time = 300)
                }
            }
        }
    }

    @Test fun notesShow() {
        val noteItem = testData.clear().insertText()

        launch {
            mainScreen {
                waitAfter(time = 500) { openNotesPage { openNoteDialog(noteItem) { onClickDelete() } } }
            }
        }
    }

    @Test fun notesHide() {
        val noteItem = testData.clear().insertTextToBin()

        launch {
            mainScreen {
                openBinPage { openNoteDialog(noteItem) { onClickRestore() } }
                openNotesPage { wait(time = 500) }
            }
        }
    }

    @Test fun binShow() {
        val noteItem = testData.clear().insertTextToBin()

        launch {
            mainScreen {
                waitAfter(time = 500) { openBinPage { openNoteDialog(noteItem) { onClickClear() } } }
            }
        }
    }

    @Test fun binHide() {
        val noteItem = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage { openNoteDialog(noteItem) { onClickDelete() } }
                openBinPage { wait(time = 500) }
            }
        }
    }

}