package sgtmelon.scriptum.test.control.anim

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test of animation info about empty list
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
                    }

                    toolbar { onEnterName(it.name).onClickAdd() }
                    repeat(times = 3) { _ ->
                        onClickCancel()
                        getSnackbar().onClickCancel()
                    }
                }
            }
        }
    }


    @Test fun notesInfoShow() = data.insertText().let {
        launch { mainScreen { notesScreen { openNoteDialog(it) { onDelete() } } } }
    }

    @Test fun notesInfoHide() = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen { openNoteDialog(it) { onRestore() } }
                notesScreen()
            }
        }
    }


    @Test fun binInfoShow() = data.insertTextToBin().let {
        launch { mainScreen { binScreen { openNoteDialog(it) { onClear() } } } }
    }

    @Test fun binInfoHide() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() } }
                binScreen()
            }
        }
    }


    @Test fun notificationInfoShowAndHide() = launch({ data.insertNotification() }) {
        mainScreen {
            notesScreen {
                openNotification {
                    repeat(times = 3) { _ ->
                        onClickCancel()
                        getSnackbar().onClickCancel()
                    }
                }
            }
        }
    }
}