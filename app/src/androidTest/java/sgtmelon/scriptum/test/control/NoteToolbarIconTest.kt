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

    @Test fun arrowBackOnCreateTextNote() = testData.createText().let {
        launch { mainScreen { openAddDialog { waitAfter(WAIT) { createTextNote(it) } } } }
    }

    @Test fun arrowBackOnCreateRollNote() = testData.createRoll().let {
        launch { mainScreen { openAddDialog { waitAfter(WAIT) { createRollNote(it) } } } }
    }

    @Test fun notAnimateOnSaveCreateTextNote() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        waitAfter(WAIT) {
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
                        waitAfter(WAIT) {
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
                        waitAfter(WAIT) { controlPanel { onClickRestoreOpen() } }
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
                        waitAfter(WAIT) { controlPanel { onClickRestoreOpen() } }
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
                                waitAfter(WAIT) { onClickEdit() }
                                waitAfter(WAIT) { onClickSave() }
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
                                waitAfter(WAIT) { onClickEdit() }
                                waitAfter(WAIT) { onClickSave() }
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
                        waitAfter(WAIT) { controlPanel { onClickEdit() } }
                        waitAfter(WAIT) { onPressBack() }
                        waitAfter(WAIT) { controlPanel { onClickEdit() } }
                        waitAfter(WAIT) { toolbar { onClickBack() } }
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
                        waitAfter(WAIT) { controlPanel { onClickEdit() } }
                        waitAfter(WAIT) { onPressBack() }
                        waitAfter(WAIT) { controlPanel { onClickEdit() } }
                        waitAfter(WAIT) { toolbar { onClickBack() } }
                    }
                }
            }
        }
    }

    private companion object {
        const val WAIT = 500L
    }

}