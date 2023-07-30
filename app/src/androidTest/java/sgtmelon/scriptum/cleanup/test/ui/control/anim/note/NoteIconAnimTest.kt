package sgtmelon.scriptum.cleanup.test.ui.control.anim.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragment
import sgtmelon.scriptum.infrastructure.screen.note.text.TextNoteFragment
import sgtmelon.scriptum.infrastructure.utils.icons.BackToCancelIcon
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test of [BackToCancelIcon] and visibleRollIcon animations for [TextNoteFragment],
 * [RollNoteFragment].
 */
@RunWith(AndroidJUnit4::class)
class NoteIconAnimTest : ParentUiTest() {

    @Test fun arrowBackOnCreateTextNote() = db.createText().let {
        launchSplash { mainScreen { openAddDialog { createText(it) } } }
    }

    @Test fun arrowBackOnCreateRollNote() = db.createRoll().let {
        launchSplash { mainScreen { openAddDialog { createRoll(it) } } }
    }


    @Test fun notAnimateOnSaveCreateTextNote() = db.createText().let {
        launchSplash {
            mainScreen {
                openAddDialog {
                    createText(it) { onEnterText(nextString()).controlPanel { onSave() } }
                }
            }
        }
    }

    @Test fun notAnimateOnSaveCreateRollNote() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        enterPanel { onAdd(nextString()) }
                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun notAnimateOnRestoreOpenTextNote() = db.insertTextToBin().let {
        launchSplash {
            mainScreen { openBin { openText(it) { controlPanel { onRestoreOpen() } } } }
        }
    }

    @Test fun notAnimateOnRestoreOpenRollNote() = db.insertRollToBin().let {
        launchSplash {
            mainScreen { openBin { openRoll(it) { controlPanel { onRestoreOpen() } } } }
        }
    }


    @Test fun animateOnEditToSaveTextNote() = db.insertText().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) { controlPanel { repeat(times = 3) { onEdit().onSave() } } }
                }
            }
        }
    }

    @Test fun animateOnEditToSaveRollNote() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) { controlPanel { repeat(times = 3) { onEdit().onSave() } } }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelTextNote() = db.insertText().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openText(it) {
                        controlPanel { onEdit() }
                        pressBack()
                        controlPanel { onEdit() }
                        toolbar { clickBack() }
                    }
                }
            }
        }
    }

    @Test fun animateOnEditToCancelRollNote() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel { onEdit() }
                        pressBack()
                        controlPanel { onEdit() }
                        toolbar { clickBack() }
                    }
                }
            }
        }
    }

    @Test fun visibleClick() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes { openRoll(it) { repeat(REPEAT_COUNT) { onClickVisible() } } }
            }
        }
    }


    companion object {
        private const val REPEAT_COUNT = 5
    }
}