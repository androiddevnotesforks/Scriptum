package sgtmelon.scriptum.ui.auto.notifications

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.notifications.NotificationsActivity
import sgtmelon.scriptum.ui.testing.parent.ParentUiRotationTest
import sgtmelon.scriptum.ui.testing.parent.launch
import sgtmelon.test.common.isDivideEntirely

/**
 * Test of [NotificationsActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotificationRotationTest : ParentUiRotationTest() {

    @Test fun contentEmpty() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                openNotifications(isEmpty = true) {
                    rotate.toSide()
                    assert(isEmpty = true)
                }
            }
        }
    }

    @Test fun contentList() = launch({ db.fillNotification() }) {
        mainScreen {
            notesScreen {
                openNotifications {
                    rotate.toSide()
                    assert(isEmpty = false)
                }
            }
        }
    }

    @Test fun snackbar() = launch({ db.insertNotification() }) {
        mainScreen {
            notesScreen {
                openNotifications {
                    repeat(times = 3) { time ->
                        onClickCancel()

                        if (time.isDivideEntirely()) {
                            rotate.toSide()
                        } else {
                            rotate.toNormal()
                        }

                        getSnackbar().onClickCancel()
                    }
                }
            }
        }
    }
}