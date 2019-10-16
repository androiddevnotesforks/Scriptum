package sgtmelon.scriptum.test.control

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.basic.extension.waitBefore
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test of animation info about empty list
 */
@RunWith(AndroidJUnit4::class)
class InfoAnimTest : ParentUiTest() {

    @Test fun rankShowAndHide() = data.rankEntity.let {
        launch {
            mainScreen {
                rankScreen(empty = true) {
                    repeat(times = 3) { _ ->
                        waitAfter(TIME) { toolbar { onEnterName(it.name).onClickAdd() } }
                        waitBefore(TIME) { onClickCancel() }
                    }
                }
            }
        }
    }


    @Test fun notesShow() = data.insertText().let {
        launch {
            mainScreen {
                waitAfter(TIME) { notesScreen { openNoteDialog(it) { onDelete() } } }
            }
        }
    }

    @Test fun notesHide() = data.insertTextToBin().let {
        launch {
            mainScreen {
                binScreen { openNoteDialog(it) { onRestore() } }
                waitAfter(TIME) { notesScreen() }
            }
        }
    }


    @Test fun binShow() = data.insertTextToBin().let {
        launch {
            mainScreen { waitAfter(TIME) { binScreen { openNoteDialog(it) { onClear() } } } }
        }
    }

    @Test fun binHide() = data.insertText().let {
        launch {
            mainScreen {
                notesScreen { openNoteDialog(it) { onDelete() } }
                waitAfter(TIME) { binScreen() }
            }
        }
    }


    @Test fun notificationShow() = launch({ data.insertNotification() }) {
        mainScreen { notesScreen { openNotification { waitAfter(TIME) { onClickCancel() } } } }
    }


    private companion object {
        const val TIME = 500L
    }

}