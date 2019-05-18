package sgtmelon.scriptum.test.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.control.menu.MenuControlAnim
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentTest

/**
 * Тест анимации кнопки у тулбара [MenuControlAnim] в [TextNoteFragment] и [RollNoteFragment]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class NoteToolbarIconTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        preference.firstStart = false
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
                    waitAfter(time = 500) { controlPanel { onClickSave() } }
                }
            }
        }
    }

    @Test fun notAnimateOnSaveCreateRollNote() = launch {
        mainScreen {
            openAddDialog {
                createRollNote {
                    enterPanel { onAddRoll(testData.rollList[0].text) }
                    waitAfter(time = 500) { controlPanel { onClickSave() } }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenTextNote() = launch({ testData.insertTextToBin() }) {
        mainScreen {
                openBinPage {
                    openTextNote {
                        waitAfter(time = 500) { controlPanel { onClickRestoreOpen() } }
                    }
                }
            }
    }

    @Test fun notAnimateOnRestoreOpenRollNote() = launch({ testData.insertRollToBin() }) {
        mainScreen {
                openBinPage {
                    openRollNote {
                        waitAfter(time = 500) { controlPanel { onClickRestoreOpen() } }
                    }
                }
            }
    }

    @Test fun animateOnEditToSaveTextNote() = launch({ testData.insertText() }) {
        mainScreen {
                openNotesPage {
                    openTextNote {
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

    @Test fun animateOnEditToSaveRollNote() = launch({ testData.insertRoll() }) {
        mainScreen {
                openNotesPage {
                    openRollNote {
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

    @Test fun animateOnEditToCancelTextNote() = launch({ testData.insertText() }) {
        mainScreen {
                openNotesPage {
                    openTextNote {
                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { onPressBack() }

                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { toolbar { onClickBack() } }
                    }
                }
            }
    }

    @Test fun animateOnEditToCancelRollNote() = launch({ testData.insertRoll() }) {
        mainScreen {
                openNotesPage {
                    openRollNote {
                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { onPressBack() }

                        waitAfter(time = 500) { controlPanel { onClickEdit() } }
                        waitAfter(time = 500) { toolbar { onClickBack() } }
                    }
                }
            }
    }

}