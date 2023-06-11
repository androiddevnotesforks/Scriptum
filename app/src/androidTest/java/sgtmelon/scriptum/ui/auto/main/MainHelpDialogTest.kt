package sgtmelon.scriptum.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest
import sgtmelon.scriptum.parent.ui.tests.launchHelpMain
import sgtmelon.scriptum.ui.cases.dialog.DialogCloseCase

/**
 * Test notifications help dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainHelpDialogTest : ParentUiTest(),
    DialogCloseCase {

    @Before override fun setUp() {
        super.setUp()
        preferencesRepo.showNotificationsHelp = true
    }

    @Test override fun close() = launchHelpMain {
        openHelpDialog {
            softClose()
            assert()
            neutral()
        }
        assert()
    }

    @Test fun displayAfterResume() = launchHelpMain {
        openHelpDialog { neutral() }
        openNotes(isEmpty = true) {
            openPreferences { clickClose() }
            assert(isEmpty = true)
        }
    }
}