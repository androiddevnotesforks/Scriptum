package sgtmelon.scriptum.cleanup.test.ui.auto.screen.note.roll

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.note.roll.RollNoteFragmentImpl

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.test.common.nextString

/**
 * Test toolbar arrow and back press for [RollNoteFragmentImpl].
 */
@RunWith(AndroidJUnit4::class)
class RollNoteBackTest : ParentUiTest() {

    @Test fun closeOnBin() = db.insertRollToBin().let {
        launchSplash {
            mainScreen {
                openBin { openRoll(it) { toolbar { clickBack() } } }
                assert()
                openBin { openRoll(it) { pressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnCreate() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openAddDialog { createRoll(it) { toolbar { clickBack() } } }
                assert()
                openAddDialog { createRoll(it) { pressBack() } }
                assert()
            }
        }
    }

    @Test fun closeOnRead() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes { openRoll(it) { toolbar { clickBack() } } }
                assert()
                openNotes { openRoll(it) { pressBack() } }
                assert()
            }
        }
    }

    @Test fun saveOnCreate() = db.createRoll().let {
        launchSplash {
            mainScreen {
                openAddDialog {
                    createRoll(it) {
                        toolbar { onEnterName(nextString()) }
                        enterPanel { onAdd(nextString()) }
                        pressBack()
                    }
                }
            }
        }
    }

    @Test fun saveOnEdit() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel { onEdit() }
                        toolbar { onEnterName(nextString()) }
                        enterPanel { onAdd(nextString()) }
                        pressBack()
                    }
                }
            }
        }
    }

    @Test fun cancelOnEdit() = db.insertRoll().let {
        launchSplash {
            mainScreen {
                openNotes {
                    openRoll(it) {
                        controlPanel { onEdit() }
                        enterPanel { onAdd(nextString()) }
                        toolbar {
                            onEnterName(nextString())
                            clickBack()
                        }
                    }
                }
            }
        }
    }
}