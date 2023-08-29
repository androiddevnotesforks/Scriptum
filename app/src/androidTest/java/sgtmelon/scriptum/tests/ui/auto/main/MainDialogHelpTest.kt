package sgtmelon.scriptum.tests.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchHelpMain
import sgtmelon.scriptum.tests.ui.control.main.MainDialogHelpTest as MainDialogHelpControlTest

/**
 * Test notifications help dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainDialogHelpTest : ParentUiRotationTest(),
    DialogCloseCase,
    DialogRotateCase {

    @Before override fun setUp() {
        super.setUp()
        preferencesRepo.showNotificationsHelp = true
    }

    @Test override fun close() = launchHelpMain {
        openHelpDialog {
            /** This dialog is not cancelable by back press. */
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

    @Test override fun rotateClose() = launchHelpMain {
        openHelpDialog {
            rotate.switch()
            assert()
            /** This dialog is not cancelable by back press. */
            softClose()
            assert()
            neutral()
        }
        assert()
    }

    /** It will be tested in [MainDialogHelpControlTest.rotateWork]. */
    override fun rotateWork() = Unit
}