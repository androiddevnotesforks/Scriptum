package sgtmelon.scriptum.tests.ui.control.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchHelpMain

/**
 * Test notifications help dialog for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainHelpDialogTest : ParentUiTest() {

    @Before override fun setUp() {
        super.setUp()
        preferencesRepo.showNotificationsHelp = true
    }

    @Test fun openSettings() = launchHelpMain { openHelpDialog { positive() } }

    @Test fun openChannel() = launchHelpMain { openHelpDialog { negative() } }

}