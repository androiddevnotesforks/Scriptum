package sgtmelon.scriptum.test.auto

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Тест для [NotificationActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class NotificationTest : ParentUiTest() {

    @Test fun contentEmpty() =
            launch { mainScreen { openNotesPage(empty = true) { openNotification(empty = true) } } }

    @Test fun contentList() = launch({ data.fillNotification() }) {
        mainScreen { openNotesPage { openNotification() } }
    }

    @Test fun listScroll() = launch({ data.fillNotification() }) {
        mainScreen { openNotesPage { openNotification { onScrollThrough() } } }
    }

    @Test fun itemCancel() = data.insertNotification(data.insertText()).let {
        launch {
            mainScreen {
                openNotesPage {
                    openNotification {
                        onClickCancel(it)
                        assert(empty = true)
                    }
                }
            }
        }
    }

    @Test fun itemCancelOnDelete() = data.insertNotification(data.insertText()).let {
        launch {
            mainScreen {
                openNotesPage {
                    openNoteDialog(it) { onClickDelete() }
                    openNotification(empty = true)
                }
            }
        }
    }

    @Test fun itemCancelFromPast() = data.insertNotification(data.insertText(), DATE_0).let {
        launch { mainScreen { openNotesPage { openNotification(empty = true) } } }
    }

    @Test fun textNoteOpen() = data.insertText().let {
        launch({ data.insertNotification(it) }) {
            mainScreen { openNotesPage { openNotification { openText(it) } } }
        }
    }

    @Test fun rollNoteOpen() = data.insertRoll().let {
        launch({ data.insertNotification(it) }) {
            mainScreen { openNotesPage { openNotification { openRoll(it) } } }
        }
    }

}