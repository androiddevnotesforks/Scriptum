package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.control.menu.MenuControlAnim
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test of [MenuControlAnim] animations for [TextNoteFragment], [RollNoteFragment]
 */
@RunWith(AndroidJUnit4::class)
class NoteIconAnimTest : ParentUiTest() {

    // TODO #TEST Анимация из стрелки в крестик при автоматическом сохранении текста/списка

    @Test fun arrowBackOnCreateTextNote() = data.createText().let {
        launch { mainScreen { addDialog { waitAfter(TIME) { createText(it) } } } }
    }

    @Test fun arrowBackOnCreateRollNote() = data.createRoll().let {
        launch { mainScreen { addDialog { waitAfter(TIME) { createRoll(it) } } } }
    }


    @Test fun notAnimateOnSaveCreateTextNote() = data.createText().let {
        launch {
            mainScreen {
                addDialog {
                    createText(it) {
                        waitAfter(TIME) {
                            onEnterText(data.textNote.text).controlPanel { onSave() }
                        }
                    }
                }
            }
        }
    }

    @Test fun notAnimateOnSaveCreateRollNote() = data.createRoll().let {
        launch {
            mainScreen {
                addDialog {
                    createRoll(it) {
                        waitAfter(TIME) {
                            enterPanel { onAddRoll(data.rollList.first().text) }
                            controlPanel { onSave() }
                        }
                    }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenTextNote() = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openTextNote(it) { waitAfter(TIME) { controlPanel { onRestoreOpen() } } }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenRollNote() = data.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openRollNote(it) { waitAfter(TIME) { controlPanel { onRestoreOpen() } } }
                }
            }
        }
    }


    @Test fun animateOnEditToSaveTextNote() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
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

    @Test fun animateOnEditToSaveRollNote() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
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
                notesScreen {
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

    @Test fun animateOnEditToCancelRollNote() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
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