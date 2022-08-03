package sgtmelon.scriptum.cleanup.test.ui.auto.rotation.notification

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.parent.ParentUiRotationTest

/**
 * Test of [NotificationActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class NotificationRotationTest : ParentUiRotationTest() {

    @Test fun contentEmpty() = launch {
        mainScreen {
            notesScreen(isEmpty = true) {
                openNotification(isEmpty = true) {
                    rotate.toSide()
                    assert(isEmpty = true)
                }
            }
        }
    }

    @Test fun contentList() = launch({ db.fillNotification() }) {
        mainScreen {
            notesScreen {
                openNotification {
                    rotate.toSide()
                    assert(isEmpty = false)
                }
            }
        }
    }

    @Test fun snackbar() = launch({ db.insertNotification() }) {
        mainScreen {
            notesScreen {
                openNotification {
                    repeat(times = 3) { time ->
                        onClickCancel()

                        if (time % 2 == 0) {
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