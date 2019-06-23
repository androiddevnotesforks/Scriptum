package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.control.menu.MenuControlAnim
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.waitAfter

/**
 * Тест анимации кнопки у тулбара [MenuControlAnim] в [TextNoteFragment] и [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class NoteToolbarIconTest : ParentUiTest() {

    override fun setUp() {
        super.setUp()

        iPreferenceRepo.firstStart = false
        testData.clear()
    }

    @Test fun arrowBackOnCreateTextNote() = testData.createText().let {
        launch { mainScreen { openAddDialog { waitAfter(time = 500) { createTextNote(it) } } } }
    }

    @Test fun arrowBackOnCreateRollNote() = testData.createRoll().let {
        launch { mainScreen { openAddDialog { waitAfter(time = 500) { createRollNote(it) } } } }
    }

    @Test fun notAnimateOnSaveCreateTextNote() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        waitAfter(time = 500) {
                            onEnterText(testData.textNote.text)
                            controlPanel { onClickSave() }
                        }
                    }
                }
            }
        }
    }

    @Test fun notAnimateOnSaveCreateRollNote() = testData.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRollNote(it) {
                        waitAfter(time = 500) {
                            enterPanel { onAddRoll(testData.rollList[0].text) }
                            controlPanel { onClickSave() }
                        }
                    }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenTextNote() = testData.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openTextNote(it) {
                        waitAfter(time = 500) { controlPanel { onClickRestoreOpen() } }
                    }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenRollNote() = testData.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openRollNote(it) {
                        waitAfter(time = 500) { controlPanel { onClickRestoreOpen() } }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToSaveTextNote() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel {
                            repeat(times = 3) {
                                waitAfter(time = 500) { onClickEdit() }
                                waitAfter(time = 500) { onClickSave() }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToSaveRollNote() = testData.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        controlPanel {
                            repeat(times = 3) {
                                waitAfter(time = 500) { onClickEdit() }
                                waitAfter(time = 500) { onClickSave() }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelTextNote() = testData.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { onPressBack() }
                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { toolbar { onClickBack() } }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelRollNote() = testData.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { onPressBack() }
                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { toolbar { onClickBack() } }
                    }
                }
            }
        }
    }

}