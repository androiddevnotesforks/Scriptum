package sgtmelon.scriptum.cleanup.test.ui.control.anim

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

import sgtmelon.scriptum.source.ui.tests.ParentUiTest

/**
 * Test of animation info about empty list.
 */
@RunWith(AndroidJUnit4::class)
class InfoAnimTest : ParentUiTest() {



    @Test fun notificationInfoShowAndHide() = launchSplash({ db.insertNotification() }) {
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