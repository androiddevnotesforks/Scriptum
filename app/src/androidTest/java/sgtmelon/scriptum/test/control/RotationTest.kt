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

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
        testData.clear()
    }

    @Test fun addDialog() = launch {
        mainScreen { openAddDialog { waitAfter(time = 5000) { assert { onDisplayContent() } } } }
    }


    @Test fun rankScreenContentEmpty() = launch {
        mainScreen {
            openRankPage(empty = true) {
                waitAfter(time = 5000) { assert { onDisplayContent(empty = true) } }
            }

            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun rankScreenContentList() = launch({ testData.fillRank() }) {
        mainScreen {
            openRankPage { waitAfter(time = 5000) { assert { onDisplayContent(empty = false) } } }
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
                        waitAfter(time = 5000) {
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
                waitAfter(time = 5000) { assert { onDisplayContent(empty = true) } }
            }

            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun notesScreenContentList() = launch({ testData.fillNotes() }) {
        mainScreen {
            openNotesPage { waitAfter(time = 5000) { assert { onDisplayContent(empty = false) } } }
            assert { onDisplayFab(visible = true) }
        }
    }

    @Test fun notesScreenTextNoteDialog() = testData.insertTextNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { waitAfter(time = 5000) { assert { onDisplayContent() } } }
                }
            }
        }
    }

    @Test fun notesScreenRollNoteDialog() = testData.insertRollNote().let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { waitAfter(time = 5000) { assert { onDisplayContent() } } }
                }
            }
        }
    }


    @Test fun binScreenContentEmpty() = launch {
        mainScreen {
            openBinPage(empty = true) {
                waitAfter(time = 5000) { assert { onDisplayContent(empty = true) } }
            }

            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun binScreenContentList() = launch({ testData.fillBin() }) {
        mainScreen {
            openBinPage { waitAfter(time = 5000) { assert { onDisplayContent(empty = false) } } }
            assert { onDisplayFab(visible = false) }
        }
    }

    @Test fun binScreenClearDialog() = launch({ testData.fillBin() }) {
        mainScreen {
            openBinPage {
                openClearDialog { waitAfter(time = 5000) { assert { onDisplayContent() } } }
            }
        }
    }

    @Test fun binScreenTextNoteDialog() = testData.insertTextNoteToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(it) { waitAfter(time = 5000) { assert { onDisplayContent() } } }
                }
            }
        }
    }

    @Test fun binScreenRollNoteDialog() = testData.insertRollNoteToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openNoteDialog(it) { waitAfter(time = 5000) { assert { onDisplayContent() } } }
                }
            }
        }
    }

}