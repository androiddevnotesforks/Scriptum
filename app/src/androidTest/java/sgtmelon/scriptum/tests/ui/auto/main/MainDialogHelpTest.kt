package sgtmelon.scriptum.tests.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchHelpMain
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase

/**
 * Test notifications help dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainDialogHelpTest : ParentUiTest(),
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