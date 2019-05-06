package sgtmelon.scriptum.test

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест работы анимации информации о пустом списке
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class InfoAnimTest : ParentTest() {

    @Test fun rankShowAndHide() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            rankScreen {
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
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch {
            MainScreen {
                waitAfter(time = 500) { notesScreen { noteDialogUi(noteItem) { onClickDelete() } } }
            }
        }
    }

    @Test fun notesHide() {
        val noteItem = testData.apply { clearAllData() }.insertTextToBin()

        afterLaunch {
            MainScreen {
                binScreen { noteDialogUi(noteItem) { onClickRestore() } }
                notesScreen { wait(time = 500) }
            }
        }
    }


    @Test fun binShow() {
        val noteItem = testData.apply { clearAllData() }.insertTextToBin()

        afterLaunch {
            MainScreen {
                waitAfter(time = 500) { binScreen { noteDialogUi(noteItem) { onClickClear() } } }
            }
        }
    }

    @Test fun binHide() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch {
            MainScreen {
                notesScreen { noteDialogUi(noteItem) { onClickDelete() } }
                binScreen { wait(time = 500) }
            }
        }
    }

}