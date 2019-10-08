package sgtmelon.scriptum.test.control.rotation.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of [RollNoteFragment] work with phone rotation
 */
@RunWith(AndroidJUnit4::class)
class RollNoteRotationTest : ParentRotationTest() {

    /**
     * Content
     */

    @Test fun contentOnBin() = data.insertRollToBin().let {
        launch { mainScreen { binScreen { openRollNote(it) { onRotate { assert() } } } } }
    }

    @Test fun contentOnRestoreOpen() = data.insertRollToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openRollNote(it) {
                        controlPanel { onRestoreOpen() }
                        onRotate { assert() }
                    }
                }
            }
        }
    }

    @Test fun contentOnCreate() = data.createRoll().let {
        launch { mainScreen { addDialog { createRoll(it) { onRotate { assert() } } } } }
    }

    @Test fun contentOnRead() = data.insertRoll().let {
        launch { mainScreen { notesScreen { openRollNote(it) { onRotate { assert() } } } } }
    }

    @Test fun contentOnEdit() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) { onRotate({ controlPanel { onEdit() } }) { assert() } }
                }
            }
        }
    }

    /**
     * Dialogs
     */

    @Test fun convertDialog() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) { controlPanel { onConvert { onRotate { assert() } } } }
                }
            }
        }
    }

    @Test fun convertDialogResult() = data.insertRoll().let {
        launch {
            mainScreen {
                notesScreen {
                    openRollNote(it) {
                        controlPanel { onConvert { onClickYes() } }
                        onRotate {
                            // TODO #TEST end assert
                        }
                    }
                }
            }
        }
    }

    @Test fun colorDialog() = data.createRoll().let {
        launch {
            mainScreen {
                addDialog {
                    createRoll(it) {
                        controlPanel {
                            onColor {
                                onClickItem()
                                onRotate { assert() }
                            }
                        }
                    }
                }
            }
        }
    }

}