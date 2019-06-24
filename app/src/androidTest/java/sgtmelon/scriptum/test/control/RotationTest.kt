package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.waitAfter

/**
 * Тест работы приложения при повороте экрана
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RotationTest : ParentUiTest() {

    // TODO запускать handler + toast для уведомления о повороте
    // TODO заменить вызов assert на более короткий

    @Test fun addDialog() = launch {
        mainScreen { openAddDialog { waitAfter(TIME) { assert { onDisplayContent() } } } }
    }


    @Test fun rankScreenContentEmpty() = launch {
        mainScreen {
            openRankPage(empty = true) {
                waitAfter(TIME) { assert { onDisplayContent(empty = true) } }
            }

            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun rankScreenContentList() = launch({ testData.fillRank() }) {
        mainScreen {
            openRankPage { waitAfter(TIME) { assert { onDisplayContent(empty = false) } } }
            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun rankScreenRenameDialog() = testData.insertRank().let {
        val newName = testData.uniqueString

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(it.name) {
                        onEnterName(newName, enabled = true)
                        waitAfter(TIME) {
                            assert { onDisplayContent(newName, enabled = true) }
                        }
                    }
                }
            }
        }
    }


    @Test fun notesScreenContentEmpty() = launch {
        mainScreen {
            openNotesPage(empty = true) {
                waitAfter(TIME) { assert { onDisplayContent(empty = true) } }
            }

            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun notesScreenContentList() = launch({ testData.fillNotes() }) {
        mainScreen {
            openNotesPage { waitAfter(TIME) { assert { onDisplayContent(empty = false) } } }
            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun notesScreenTextNoteDialog() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { waitAfter(TIME) { assert { onDisplayContent() } } }
                }
            }
        }
    }

    @Test fun notesScreenRollNoteDialog() = testData.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { waitAfter(TIME) { assert { onDisplayContent() } } }
                }
            }
        }
    }


    @Test fun binScreenContentEmpty() = launch {
        mainScreen {
            openBinPage(empty = true) {
                waitAfter(TIME) { assert { onDisplayContent(empty = true) } }
            }

            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun binScreenContentList() = launch({ testData.fillBin() }) {
        mainScreen {
            openBinPage { waitAfter(TIME) { assert { onDisplayContent(empty = false) } } }
            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun binScreenClearDialog() = launch({ testData.fillBin() }) {
        mainScreen {
            openBinPage {
                openClearDialog { waitAfter(TIME) { assert { onDisplayContent() } } }
            }
        }
    }

    @Test fun binScreenTextNoteDialog() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(it) { waitAfter(TIME) { assert { onDisplayContent() } } }
                }
            }
        }
    }

    @Test fun binScreenRollNoteDialog() = testData.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(it) { waitAfter(TIME) { assert { onDisplayContent() } } }
                }
            }
        }
    }


    @Test fun notificationScreenContentEmpty() = launch {
        mainScreen {
            openNotesPage(empty = true) {
                openNotification(empty = true) {
                    waitAfter(TIME) { assert { onDisplayContent(empty = true) } }
                }
            }
        }
    }

    @Test fun notificationScreenContentList() = launch({ testData.fillNotification() }) {
        mainScreen {
            openNotesPage {
                openNotification { waitAfter(TIME) { assert { onDisplayContent(empty = false) } } }
            }
        }
    }


    private companion object {
        const val TIME = 7000L
    }

}