package sgtmelon.scriptum.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Тест работы приложения при повороте экрана
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RotationTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
    }

    @Test fun addDialog() = afterLaunch {
        MainScreen { addDialog { wait(time = 5000) { assert { onDisplayContent() } } } }
    }


    @Test fun notesScreenContentEmpty() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = true) }
                wait(time = 5000) { assert { onDisplayContent(empty = true) } }
            }
        }
    }

    @Test fun notesScreenContentList() {
        beforeLaunch { testData.apply { clearAllData() }.fillNotes() }

        MainScreen {
            notesScreen {
                assert { onDisplayContent(empty = false) }
                wait(time = 5000) { assert { onDisplayContent(empty = false) } }
            }
        }
    }

    @Test fun notesScreenTextNoteDialog() {
        val noteItem = testData.apply { clearAllData() }.insertText()

        afterLaunch {
            MainScreen {
                notesScreen {
                    noteDialog(noteItem) {
                        wait(time = 5000) { assert { onDisplayContent(noteItem) } }
                    }
                }
            }
        }
    }

    @Test fun notesScreenRollNoteDialog() {
        val noteItem = testData.apply { clearAllData() }.insertRoll()

        afterLaunch {
            MainScreen {
                notesScreen {
                    noteDialog(noteItem) {
                        wait(time = 5000) { assert { onDisplayContent(noteItem) } }
                    }
                }
            }
        }
    }


    @Test fun binScreenContentEmpty() {
        beforeLaunch { testData.clearAllData() }

        MainScreen {
            binScreen {
                assert { onDisplayContent(empty = true) }
                wait(time = 5000) { assert { onDisplayContent(empty = true) } }
            }
        }
    }

    @Test fun binScreenContentList() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin() }

        MainScreen {
            binScreen {
                assert { onDisplayContent(empty = false) }
                wait(time = 5000) { assert { onDisplayContent(empty = false) } }
            }
        }
    }

    @Test fun binScreenClearDialog() {
        beforeLaunch { testData.apply { clearAllData() }.fillBin() }

        MainScreen {
            binScreen {
                clearDialog {
                    assert { onDisplayContent() }
                    wait(time = 5000) { assert { onDisplayContent() } }
                }
            }
        }
    }

    @Test fun binScreenTextNoteDialog() {
        val noteItem = testData.apply { clearAllData() }.insertTextToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    noteDialog(noteItem) {
                        wait(time = 5000) { assert { onDisplayContent(noteItem) } }
                    }
                }
            }
        }
    }

    @Test fun binScreenRollNoteDialog() {
        val noteItem = testData.apply { clearAllData() }.insertRollToBin()

        afterLaunch {
            MainScreen {
                binScreen {
                    noteDialog(noteItem) {
                        wait(time = 5000) { assert { onDisplayContent(noteItem) } }
                    }
                }
            }
        }
    }

}