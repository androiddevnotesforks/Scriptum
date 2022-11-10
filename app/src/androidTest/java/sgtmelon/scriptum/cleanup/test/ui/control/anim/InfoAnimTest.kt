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
                rankScreen(isEmpty = true) {
                    repeat(times = 3) {
                        toolbar { onEnterName(entity.name).onClickAdd() }
                        onClickCancel()
                        assert(isEmpty = true)
                    }

                    toolbar { onEnterName(entity.name).onClickAdd() }
                    repeat(times = 3) {
                        onClickCancel()
                        assert(isEmpty = true)
                        getSnackbar().clickCancel()
                        assert(isEmpty = false)
                    }
                }
            }
        }
    }


    @Test fun notesInfoShow() = db.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) { controlPanel { onDelete() } }
                    assert(isEmpty = true)
                }
            }
        }
    }

    @Test fun notesInfoHide() = db.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openNoteDialog(it) { onRestore() } }
                notesScreen()
            }
        }
    }


    @Test fun binInfoShow() = db.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openTextNote(it) { controlPanel { onClear() } }
                    assert(isEmpty = true)
                }
            }
        }
    }

    @Test fun binInfoHide() = db.insertText().let {
        launch {
            mainScreen {
                binScreen(isEmpty = true)
                notesScreen { openNoteDialog(it) { onDelete() } }
                binScreen()
            }
        }
    }


    @Test fun notificationInfoShowAndHide() = launch({ db.insertNotification() }) {
        mainScreen {
            notesScreen {
                openNotifications {
                    repeat(times = 3) {
                        itemCancel()
                        assert(isEmpty = true)
                        getSnackbar().clickCancel()
                        assert(isEmpty = false)
                    }
                }
            }
        }
    }
}