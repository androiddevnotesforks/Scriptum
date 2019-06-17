package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentTest

/**
 * Тест работы приложения при повороте экрана
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class RotationTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
    }

    @Test fun addDialog() = launch {
        mainScreen { openAddDialog { waitBefore(time = 5000) { assert { onDisplayContent() } } } }
    }


    @Test fun rankScreenContentEmpty() = launch({ testData.clear() }) {
        mainScreen {
            openRankPage {
                assert { onDisplayContent(empty = true) }
                waitBefore(time = 5000) { assert { onDisplayContent(empty = true) } }
            }

            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun rankScreenContentList() = launch({ testData.clear().fillRank() }) {
        mainScreen {
            openRankPage {
                assert { onDisplayContent(empty = false) }
                waitBefore(time = 5000) { assert { onDisplayContent(empty = false) } }
            }

            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun rankScreenRenameDialog() {
        val rankItem = testData.clear().insertRank()
        val newName = testData.uniqueString

        launch {
            mainScreen {
                openRankPage {
                    openRenameDialog(rankItem.name) {
                        onEnterName(newName)

                        assert { onDisplayContent(newName) }
                        waitBefore(time = 5000) { assert { onDisplayContent(newName) } }
                    }
                }
            }
        }
    }


    @Test fun notesScreenContentEmpty() = launch({ testData.clear() }) {
        mainScreen {
            openNotesPage {
                assert { onDisplayContent(empty = true) }
                waitBefore(time = 5000) { assert { onDisplayContent(empty = true) } }
            }

            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun notesScreenContentList() = launch({ testData.clear().fillNotes() }) {
        mainScreen {
            openNotesPage {
                assert { onDisplayContent(empty = false) }
                waitBefore(time = 5000) { assert { onDisplayContent(empty = false) } }
            }

            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun notesScreenTextNoteDialog() {
        val noteEntity = testData.clear().insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteEntity) {
                        waitBefore(time = 5000) { assert { onDisplayContent(noteEntity) } }
                    }
                }
            }
        }
    }

    @Test fun notesScreenRollNoteDialog() {
        val noteEntity = testData.clear().insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(noteEntity) {
                        waitBefore(time = 5000) { assert { onDisplayContent(noteEntity) } }
                    }
                }
            }
        }
    }


    @Test fun binScreenContentEmpty() = launch({ testData.clear() }) {
        mainScreen {
            openBinPage {
                assert { onDisplayContent(empty = true) }
                waitBefore(time = 5000) { assert { onDisplayContent(empty = true) } }
            }

            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun binScreenContentList() = launch({ testData.clear().fillBin() }) {
        mainScreen {
            openBinPage {
                assert { onDisplayContent(empty = false) }
                waitBefore(time = 5000) { assert { onDisplayContent(empty = false) } }
            }

            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun binScreenClearDialog() = launch({ testData.clear().fillBin() }) {
        mainScreen {
            openBinPage {
                openClearDialog {
                    assert { onDisplayContent() }
                    waitBefore(time = 5000) { assert { onDisplayContent() } }
                }
            }
        }
    }

    @Test fun binScreenTextNoteDialog() {
        val noteEntity = testData.clear().insertTextToBin()

        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(noteEntity) {
                        waitBefore(time = 5000) { assert { onDisplayContent(noteEntity) } }
                    }
                }
            }
        }
    }

    @Test fun binScreenRollNoteDialog() {
        val noteEntity = testData.clear().insertRollToBin()

        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(noteEntity) {
                        waitBefore(time = 5000) { assert { onDisplayContent(noteEntity) } }
                    }
                }
            }
        }
    }

}