package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.control.menu.MenuControlAnim
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest
import sgtmelon.scriptum.basic.waitAfter

/**
 * Test of [MenuControlAnim] animations for [TextNoteFragment], [RollNoteFragment]
 */
@RunWith(AndroidJUnit4::class)
class NoteToolbarIconTest : ParentUiTest() {

    // TODO Анимация из стрелки в крестик при автоматическом сохранении текста/списка

    @Test fun arrowBackOnCreateTextNote() = data.createText().let {
        launch { mainScreen { openAddDialog { waitAfter(TIME) { createTextNote(it) } } } }
    }

    // TODO #FIX_TEST
    @Test fun arrowBackOnCreateRollNote() = data.createRoll().let {
        launch { mainScreen { openAddDialog { waitAfter(TIME) { createRollNote(it) } } } }
    }


    @Test fun notAnimateOnSaveCreateTextNote() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createTextNote(it) {
                        waitAfter(TIME) {
                            onEnterText(data.textNote.text)
                            controlPanel { onSave() }
                        }
                    }
                }
            }
        }
    }

    // TODO #FIX_TEST
    @Test fun notAnimateOnSaveCreateRollNote() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRollNote(it) {
                        waitAfter(TIME) {
                            enterPanel { onAddRoll(data.rollList.first().text) }
                            controlPanel { onSave() }
                        }
                    }
                }
            }
        }
    }

    // TODO #FIX_TEST
    @Test fun notAnimateOnRestoreOpenTextNote() = data.insertTextToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openTextNote(it) {
                        waitAfter(TIME) { controlPanel { onRestoreOpen() } }
                    }
                }
            }
        }
    }

    // TODO #FIX_TEST
    @Test fun notAnimateOnRestoreOpenRollNote() = data.insertRollToBin().let {
        launch {
            mainScreen {
                openBinPage {
                    openRollNote(it) {
                        waitAfter(TIME) { controlPanel { onRestoreOpen() } }
                    }
                }
            }
        }
    }


    @Test fun animateOnEditToSaveTextNote() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        controlPanel {
                            repeat(times = 3) {
                                waitAfter(TIME) { onEdit() }
                                waitAfter(TIME) { onSave() }
                            }
                        }
                    }
                }
            }
        }
    }

    // TODO #FIX_TEST
    @Test fun animateOnEditToSaveRollNote() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        controlPanel {
                            repeat(times = 3) {
                                waitAfter(TIME) { onEdit() }
                                waitAfter(TIME) { onSave() }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelTextNote() = data.insertText().let {
        launch {
            mainScreen {
                openNotesPage {
                    openTextNote(it) {
                        waitAfter(TIME) { controlPanel { onEdit() } }
                        waitAfter(TIME) { onPressBack() }
                        waitAfter(TIME) { controlPanel { onEdit() } }
                        waitAfter(TIME) { toolbar { onClickBack() } }
                    }
                }
            }
        }
    }

    // TODO #FIX_TEST
    @Test fun animateOnEditToCancelRollNote() = data.insertRoll().let {
        launch {
            mainScreen {
                openNotesPage {
                    openRollNote(it) {
                        waitAfter(TIME) { controlPanel { onEdit() } }
                        waitAfter(TIME) { onPressBack() }
                        waitAfter(TIME) { controlPanel { onEdit() } }
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