package sgtmelon.scriptum.test.auto.rotation.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.test.ParentRotationTest

/**
 * Test of [NotificationActivity] work with phone rotation
 */
@RunWith(AndroidJUnit4::class)
class NotificationRotationTest : ParentRotationTest() {

    @Test fun contentEmpty() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                openNotification(isEmpty = true) {
                    automator?.rotateSide()
                    assert(isEmpty = true)
                }
            }
        }
    }

    @Test fun contentList() = launch({ data.fillNotification() }) {
        mainScreen {
            notesScreen {
                openNotification {
                    automator?.rotateSide()
                    assert(isEmpty = false)
                }
            }
        }
    }

    @Test fun snackbar() = launch({ data.insertNotification() }) {
        mainScreen {
            notesScreen {
                openNotification {
                    onClickCancel()
                    automator?.rotateSide()
                    getSnackbar().onClickCancel()
                }
            }
        }
    }
}