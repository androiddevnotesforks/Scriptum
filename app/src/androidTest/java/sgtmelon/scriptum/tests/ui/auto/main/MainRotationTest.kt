package sgtmelon.scriptum.tests.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchHelpMain
import sgtmelon.scriptum.source.ui.tests.launchMain

/**
 * Test of [MainActivity] work with phone rotation.
 */
@RunWith(AndroidJUnit4::class)
class MainRotationTest : ParentUiRotationTest() {

    @Test fun helpDialog() = launchHelpMain({ preferencesRepo.showNotificationsHelp = true }) {
        openHelpDialog {
            rotate.toSide()
            assert()
        }
    }

}