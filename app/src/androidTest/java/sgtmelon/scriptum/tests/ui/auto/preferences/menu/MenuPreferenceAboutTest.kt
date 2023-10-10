package sgtmelon.scriptum.tests.ui.auto.preferences.menu

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.source.cases.dialog.DialogCloseCase
import sgtmelon.scriptum.source.cases.dialog.DialogRotateCase
import sgtmelon.scriptum.source.cases.dialog.DialogWorkCase
import sgtmelon.scriptum.source.ui.screen.dialogs.preference.AboutDialogUi
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMenuPreference

/**
 * Test for [MenuPreferenceFragment] and [AboutDialogUi].
 */
@RunWith(AndroidJUnit4::class)
class MenuPreferenceAboutTest : ParentUiRotationTest(),
    DialogWorkCase,
    DialogCloseCase,
    DialogRotateCase {

    @Test override fun work() = launchMenuPreference { openAboutDialog { sendEmail() } }

    @Test override fun close() = launchMenuPreference {
        openAboutDialog { softClose() }
        assert()
    }

    @Test override fun rotateClose() = launchMenuPreference {
        openAboutDialog {
            rotate.switch()
            assert()
            softClose()
        }

        assert()
    }

    @Test override fun rotateWork() = launchMenuPreference {
        openAboutDialog {
            rotate.switch()
            assert()
            sendEmail()
        }
    }
}