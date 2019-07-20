package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.control.menu.MenuControlAnim
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
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
        launch { mainScreen { openAddDialog { waitAfter(TIME) { createTextNote(it) } } } }
    }

    @Test fun arrowBackOnCreateRollNote() = testData.createRoll().let {
        launch { mainScreen { openAddDialog { waitAfter(TIME) { createRollNote(it) } } } }
    }

    @Test fun notAnimateOnSaveCreateTextNote() = testData.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        waitAfter(TIME) {
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
                        waitAfter(TIME) {
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
                        waitAfter(TIME) { controlPanel { onClickRestoreOpen() } }
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
                        waitAfter(TIME) { controlPanel { onClickRestoreOpen() } }
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
                                waitAfter(TIME) { onClickEdit() }
                                waitAfter(TIME) { onClickSave() }
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
                                waitAfter(TIME) { onClickEdit() }
                                waitAfter(TIME) { onClickSave() }
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
                        waitAfter(TIME) { controlPanel { onClickEdit() } }
                        waitAfter(TIME) { onPressBack() }
                        waitAfter(TIME) { controlPanel { onClickEdit() } }
                        waitAfter(TIME) { toolbar { onClickBack() } }
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
                        waitAfter(TIME) { controlPanel { onClickEdit() } }
                        waitAfter(TIME) { onPressBack() }
                        waitAfter(TIME) { controlPanel { onClickEdit() } }
                        waitAfter(TIME) { toolbar { onClickBack() } }
                    }
                }
            }
        }
    }

    private companion object {
        const val TIME = 500L
    }

}