package sgtmelon.scriptum.tests.ui.control.preference

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.presentation.dialog.AboutDialog
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMenuPreference
import sgtmelon.scriptum.tests.ui.auto.preferences.menu.MenuPreferenceRotationTest
import sgtmelon.test.cappuccino.utils.await

/**
 * Test [AboutDialog] in [MenuPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class PreferenceDialogAboutTest : ParentUiRotationTest(),
    DialogRotateCase {

    @After override fun tearDown() {
        super.tearDown()
        await(END_DELAY)
    }

    @Test fun work() = launchMenuPreference { openAboutDialog { sendEmail() } }

    /** It will be tested in [MenuPreferenceRotationTest.aboutDialog]. */
    override fun rotateClose() = Unit

    @Test override fun rotateWork() = launchMenuPreference {
        openAboutDialog {
            rotate.toSide()
            sendEmail()
        }
    }

    companion object {
        private const val END_DELAY = 2000L
    }
}