package sgtmelon.scriptum.cleanup.test.ui.control.anim.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.presentation.control.toolbar.icon.NavigationIconControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest

/**
 * Test of [NavigationIconControl] and visibleRollIcon animations for [TextNoteFragment],
 * [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class NoteIconAnimTest : ParentUiTest() {

    @Test fun arrowBackOnCreateTextNote() = data.createText().let {
        launch { mainScreen { openAddDialog { createText(it) } } }
    }

    @Test fun arrowBackOnCreateRollNote() = data.createRoll().let {
        launch { mainScreen { openAddDialog { createRoll(it) } } }
    }


    @Test fun notAnimateOnSaveCreateTextNote() = data.createText().let {
        launch {
            mainScreen {
                openAddDialog {
                    createText(it) { onEnterText(nextString()).controlPanel { onSave() } }
                }
            }
        }
    }

    @Test fun notAnimateOnSaveCreateRollNote() = data.createRoll().let {
        launch {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        enterPanel { onAdd(data.rollList.first().text) }
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenTextNote() = data.insertTextToBin().let {
        launch {
            mainScreen { binScreen { openTextNote(it) { controlPanel { onRestoreOpen() } } } }
        }
    }

    @Test fun notAnimateOnRestoreOpenRollNote() = data.insertRollToBin().let {
        launch {
            mainScreen { binScreen { openRollNote(it) { controlPanel { onRestoreOpen() } } } }
        }
    }


    @Test fun animateOnEditToSaveTextNote() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) { controlPanel { repeat(times = 3) { onEdit().onSave() } } }
                }
            }
        }
    }

    @Test fun animateOnEditToSaveRollNote() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) { controlPanel { repeat(times = 3) { onEdit().onSave() } } }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelTextNote() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) {
                        controlPanel { onEdit() }
                        onPressBack()
                        controlPanel { onEdit() }
                        toolbar { onClickBack() }
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
                        controlPanel { onEdit() }
                        onPressBack()
                        controlPanel { onEdit() }
                        toolbar { onClickBack() }
                    }
                }
            }
        }
    }

    @Test fun visibleClick() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen { openRollNote(it) { repeat(REPEAT_COUNT) { onClickVisible() } } }
            }
        }
    }


    companion object {
        private const val REPEAT_COUNT = 5
    }
}