package sgtmelon.scriptum.cleanup.test.ui.control.anim

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test of animation info about empty list.
 */
@RunWith(AndroidJUnit4::class)
class InfoAnimTest : ParentUiTest() {

    @Test fun rankInfoShowAndHide() = db.rankEntity.let { entity ->
        launch {
            mainScreen {
                openRank(isEmpty = true) {
                    repeat(times = 3) {
                        toolbar { enter(entity.name).addToEnd() }
                        itemCancel()
                        assert(isEmpty = true)
                    }

                    toolbar { enter(entity.name).addToEnd() }
                    repeat(times = 3) {
                        itemCancel()
                        assert(isEmpty = true)
                        snackbar { action() }
                        assert(isEmpty = false)
                    }
                }
            }
        }
    }


    @Test fun notesInfoShow() = db.insertText().let {
        launch {
            mainScreen {
                openNotes {
                    openText(it) { controlPanel { onDelete() } }
                    assert(isEmpty = true)
                }
            }
        }
    }

    @Test fun notesInfoHide() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openNotes(isEmpty = true)
                openBin { openNoteDialog(it) { restore() } }
                openNotes()
            }
        }
    }


    @Test fun binInfoShow() = db.insertTextToBin().let {
        launch {
            mainScreen {
                openBin {
                    openText(it) { controlPanel { onClear() } }
                    assert(isEmpty = true)
                }
            }
        }
    }

    @Test fun binInfoHide() = db.insertText().let {
        launch {
            mainScreen {
                openBin(isEmpty = true)
                openNotes { openNoteDialog(it) { delete() } }
                openBin()
            }
        }
    }


    @Test fun notificationInfoShowAndHide() = launch({ db.insertNotification() }) {
        mainScreen {
            openNotes {
                openNotifications {
                    repeat(times = 3) {
                        itemCancel()
                        assert(isEmpty = true)
                        snackbar { action() }
                        assert(isEmpty = false)
                    }
                }
            }
        }
    }
}