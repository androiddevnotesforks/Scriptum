package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.control.menu.MenuControlAnim
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

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

    @Test fun arrowBackOnCreateTextNote() = launch {
        mainScreen { openAddDialog { createTextNote { wait(time = 500) } } }
    }

    @Test fun arrowBackOnCreateRollNote() = launch {
        mainScreen { openAddDialog { createRollNote { wait(time = 500) } } }
    }

    @Test fun notAnimateOnSaveCreateTextNote() = launch {
        mainScreen {
            openAddDialog {
                createTextNote {
                    onEnterText(testData.textNote.text)
                    controlPanel { onClickSave() }
                    wait(time = 500)
                }
            }
        }
    }

    @Test fun notAnimateOnSaveCreateRollNote() = launch {
        mainScreen {
            openAddDialog {
                createRollNote {
                    enterPanel { onAddRoll(testData.rollList[0].text) }
                    controlPanel { onClickSave() }
                    wait(time = 500)
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenTextNote() {
        val noteEntity = testData.insertTextToBin()

        launch {
            mainScreen {
                openBinPage {
                    openTextNote(noteEntity) {
                        controlPanel { onClickRestoreOpen() }
                        wait(time = 500)
                    }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenRollNote() {
        val noteEntity = testData.insertRollToBin()

        launch {
            mainScreen {
                openBinPage {
                    openRollNote(noteEntity) {
                        controlPanel { onClickRestoreOpen() }
                        wait(time = 500)
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToSaveTextNote() {
        val noteEntity = testData.insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(noteEntity) {
                        controlPanel {
                            repeat(times = 3) {
                                onClickEdit()
                                wait(time = 500)
                                onClickSave()
                                wait(time = 500)
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToSaveRollNote() {
        val noteEntity = testData.insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(noteEntity) {
                        controlPanel {
                            repeat(times = 3) {
                                onClickEdit()
                                wait(time = 500)
                                onClickSave()
                                wait(time = 500)
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelTextNote() {
        val noteEntity = testData.insertText()

        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(noteEntity) {
                        controlPanel { onClickEdit() }
                        wait(time = 500)
                        onPressBack()
                        wait(time = 500)

                        controlPanel { onClickEdit() }
                        wait(time = 500)
                        toolbar { onClickBack() }
                        wait(time = 500)
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelRollNote() {
        val noteEntity = testData.insertRoll()

        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(noteEntity) {
                        controlPanel { onClickEdit() }
                        wait(time = 500)
                        onPressBack()
                        wait(time = 500)

                        controlPanel { onClickEdit() }
                        wait(time = 500)
                        toolbar { onClickBack() }
                        wait(time = 500)
                    }
                }
            }
        }
    }

}