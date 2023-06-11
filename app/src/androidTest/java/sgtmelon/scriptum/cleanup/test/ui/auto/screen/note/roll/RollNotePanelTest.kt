package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragmentImpl

import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test control panel for [RollNoteFragmentImpl].
 */
@RunWith(AndroidJUnit4::class)
class RollNotePanelTest : ParentUiTest() {

    @Test fun actionOnBinRestore() = db.insertRollToBin().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true)
                openBin {
                    openRoll(it) { controlPanel { onRestore() } }
                    assert(isEmpty = true)
                }
                openNotes()
            }
        }
    }

    @Test fun actionOnBinRestoreOpen() = db.insertRollToBin().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true)

                openBin {
                    openRoll(it) {
                        controlPanel { onRestoreOpen() }
                        pressBack()
                    }
                    assert(isEmpty = true)
                }

                openNotes()
            }
        }
    }

    @Test fun actionOnBinClear() = db.insertRollToBin().let {
        launchSplash {
            mainScreen {
                openNotes(isEmpty = true)
                openBin {
                    openRoll(it) { controlPanel { onClear() } }
                    assert(isEmpty = true)
                }
                openNotes(isEmpty = true)
            }
        }
    }


    @Test fun actionOnReadNotification() = db.insertRoll().let {
        launchSplash {
            mainScreen { openNotes { openRoll(it) { controlPanel { onNotification() } } } }
        }
    }

    @Test fun actionOnReadBind() = startBindTest(isStatus = false)

    @Test fun actionOnReadUnbind() = startBindTest(isStatus = true)

    private fun startBindTest(isStatus: Boolean) {
        val model = db.insertRoll(db.rollNote.copy(isStatus = isStatus))

        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(model) {
                        controlPanel { onBind() }
                        pressBack()
                    }

                    openNoteDialog(model)
                }
            }
        }
    }

    @Test fun actionOnReadConvert() = db.insertRoll().let {
        launchSplash {
            mainScreen { openNotes { openRoll(it) { controlPanel { onConvert() } } } }
        }
    }

    @Test fun actionOnReadDelete() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openBin(isEmpty = true)

                openNotes {
                    openRoll(it) { controlPanel { onDelete() } }
                    assert(isEmpty = true)
                }

                openBin()
            }
        }
    }

    @Test fun actionOnReadEdit() = db.insertRoll().let {
        launchSplash { mainScreen { openNotes { openRoll(it) { controlPanel { onEdit() } } } } }
    }


    // TODO finish test
    @Test fun actionOnEditUndoRedo() {
        TODO(reason = "#TEST write test")
    }

    @Test fun actionOnCreateRank() = db.fillRank(count = 3).let {
        val item = db.createRoll()
        launchSplash {
            mainScreen {
                openAddDialog {
                    createRoll(item, isRankEmpty = false) { controlPanel { onRank(it) } }
                }
            }
        }
    }

    @Test fun actionOnEditRank() = db.fillRank(count = 3).let {
        val item = db.insertRoll()
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(item, isRankEmpty = false) {
                        controlPanel { onEdit().onRank(it) }
                    }
                }
            }
        }
    }

    @Test fun actionOnCreateColor() = db.createRoll().let {
        launchSplash { mainScreen { openAddDialog { createRoll(it) { controlPanel { onColor() } } } } }
    }

    @Test fun actionOnEditColor() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes { openRoll(it) { controlPanel { onEdit().onColor() } } }
            }
        }
    }


    @Test fun actionOnCreateSave() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        toolbar { onEnterName(nextString()) }
                        repeat(times = 3) { enterPanel { onAdd(nextString()) } }
                        onSwipe()

                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun actionOnEditSave() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel { onEdit() }

                        toolbar { onEnterName(nextString()) }
                        onSwipeAll()
                        repeat(times = 3) { enterPanel { onAdd(nextString()) } }

                        controlPanel { onSave() }
                    }
                }
            }
        }
    }

    @Test fun actionOnCreateLongSave() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        toolbar { onEnterName(nextString()) }
                        repeat(times = 3) { enterPanel { onAdd(nextString()) } }
                        onSwipe()

                        controlPanel { onLongSave() }
                        toolbar { clickBack() }
                    }
                }
            }
        }
    }

    @Test fun actionOnEditLongSave() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel { onEdit() }

                        toolbar { onEnterName(nextString()) }
                        onSwipeAll()
                        repeat(times = 3) { enterPanel { onAdd(nextString()) } }

                        controlPanel { onLongSave() }
                        toolbar { clickBack() }
                    }
                }
            }
        }
    }
}