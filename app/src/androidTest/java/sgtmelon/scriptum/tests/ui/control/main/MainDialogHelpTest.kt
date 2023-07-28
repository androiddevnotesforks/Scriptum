package sgtmelon.scriptum.tests.ui.control.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchHelpMain
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.common.halfChance
import sgtmelon.scriptum.tests.ui.auto.main.MainDialogHelpTest as MainDialogHelpAutoTest

/**
 * Test notifications help dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainDialogHelpTest : ParentUiRotationTest(),
    DialogRotateCase {

    @Before override fun setUp() {
        super.setUp()
        preferencesRepo.showNotificationsHelp = true
    }

    @After override fun tearDown() {
        super.tearDown()
        await(END_DELAY)
    }

    @Test fun openSettings() = launchHelpMain { openHelpDialog { positive() } }

    @Test fun openChannel() = launchHelpMain { openHelpDialog { negative() } }

    /** It will be tested in [MainDialogHelpAutoTest.rotateClose]. */
    override fun rotateClose() = Unit

    @Test override fun rotateWork() = launchHelpMain {
        openHelpDialog {
            rotate.toSide()
            if (halfChance()) positive() else negative()
        }
    }

    companion object {
        private const val END_DELAY = 2000L
    }
}