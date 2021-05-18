package sgtmelon.scriptum.test.auto.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [NotificationActivity]
 */
@RunWith(AndroidJUnit4::class)
class NotificationTest : ParentUiTest() {

    @Test fun close() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                openNotification(isEmpty = true) { onPressBack() }
                openNotification(isEmpty = true) { onClickClose() }
            }
        }
    }

    @Test fun contentEmpty() = launch {
        mainScreen { notesScreen(isEmpty = true) { openNotification(isEmpty = true) } }
    }

    @Test fun contentList() = launch({ data.fillNotification() }) {
        mainScreen { notesScreen { openNotification() } }
    }

    @Test fun listScroll() = launch({ data.fillNotification() }) {
        mainScreen { notesScreen { openNotification { onScrollThrough() } } }
    }


    @Test fun textNoteOpen() = data.insertText().let {
        launch({ data.insertNotification(it) }) {
            mainScreen { notesScreen { openNotification { openText(it) } } }
        }
    }

    @Test fun rollNoteOpen() = data.insertRoll().let {
        launch({ data.insertNotification(it) }) {
            mainScreen { notesScreen { openNotification { openRoll(it) } } }
        }
    }


    @Test fun itemCancel() = data.insertNotification().let {
        launch {
            mainScreen { notesScreen { openNotification { onClickCancel().assert(isEmpty = true) } } }
        }
    }

    @Test fun itemCancelOnDelete() = data.insertNotification().let {
        launch {
            mainScreen {
                notesScreen {
                    openNoteDialog(it) { onDelete() }.openNotification(isEmpty = true)
                }
            }
        }
    }

    /**
     * androidx.test.espresso.base.DefaultFailureHandler$AssertionFailedWithCauseError: 'is displayed on the screen to the user' doesn't match the selected view.
     * Expected: is displayed on the screen to the user
     * Got: "LinearLayout{id=2131296560, res-name=notification_info_include, visibility=GONE, width=0, height=0, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=true, is-selected=false, layout-params=android.widget.FrameLayout$LayoutParams@f08ca5e, tag=null, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=0.0, child-count=3}"
     */
    @Test fun itemCancelFromPast() = data.insertNotification(date = DATE_5).let {
        launch { mainScreen { notesScreen { openNotification(isEmpty = true) } } }
    }

}