package sgtmelon.scriptum.test.control

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест работы анимации информации о пустом списке
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class InfoAnimTest : ParentUiTest() {

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

                    wait(time = 300) { onClickCancel(name) }
                    wait(time = 300)
                }
            }
        }
    }

    @Test fun notesShow() {
        val noteEntity = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage { openNoteDialog(noteEntity) { onClickDelete() } }
                wait(time = 500)
            }
        }
    }

    @Test fun notesHide() {
        val noteEntity = testData.clear().insertTextToBin()

        launch {
            mainScreen {
                openBinPage { openNoteDialog(noteEntity) { onClickRestore() } }
                openNotesPage { wait(time = 500) }
            }
        }
    }

    @Test fun binShow() {
        val noteEntity = testData.clear().insertTextToBin()

        launch {
            mainScreen {
                openBinPage { openNoteDialog(noteEntity) { onClickClear() } }
                wait(time = 500)
            }
        }
    }

    @Test fun binHide() {
        val noteEntity = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage { openNoteDialog(noteEntity) { onClickDelete() } }
                openBinPage { wait(time = 500) }
            }
        }
    }

}