package sgtmelon.scriptum.test.control.anim

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.parent.ParentUiTest

/**
 * Test of animation info about empty list.
 */
@RunWith(AndroidJUnit4::class)
class InfoAnimTest : ParentUiTest() {

    @Test fun rankInfoShowAndHide() = data.rankEntity.let {
        launch {
            mainScreen {
                rankScreen(isEmpty = true) {
                    repeat(times = 3) { _ ->
                        toolbar { onEnterName(it.name).onClickAdd() }
                        onClickCancel()
                        assert(isEmpty = true)
                    }

                    toolbar { onEnterName(it.name).onClickAdd() }
                    repeat(times = 3) { _ ->
                        onClickCancel()
                        assert(isEmpty = true)
                        getSnackbar().onClickCancel()
                        assert(isEmpty = false)
                    }
                }
            }
        }
    }


    @Test fun notesInfoShow() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen {
                    openTextNote(it) { controlPanel { onDelete() } }
                    assert(isEmpty = true)
                }
            }
        }
    }

    @Test fun notesInfoHide() = data.insertTextToBin().let {
        launch {
            mainScreen {
                notesScreen(isEmpty = true)
                binScreen { openNoteDialog(it) { onRestore() } }
                notesScreen()
            }
        }
    }


    @Test fun binInfoShow() = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen {
                    openTextNote(it) { controlPanel { onClear() } }
                    assert(isEmpty = true)
                }
            }
        }
    }

    @Test fun binInfoHide() = data.insertText().let {
        launch {
            mainScreen {
                binScreen(isEmpty = true)
                notesScreen { openNoteDialog(it) { onDelete() } }
                binScreen()
            }
        }
    }


    @Test fun notificationInfoShowAndHide() = launch({ data.insertNotification() }) {
        mainScreen {
            notesScreen {
                openNotification {
                    repeat(times = 3) {
                        onClickCancel()
                        assert(isEmpty = true)
                        getSnackbar().onClickCancel()
                        assert(isEmpty = false)
                    }
                }
            }
        }
    }
}