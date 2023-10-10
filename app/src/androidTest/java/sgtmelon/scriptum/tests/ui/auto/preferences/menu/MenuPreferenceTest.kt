package sgtmelon.scriptum.tests.ui.auto.preferences.menu

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.screen.preference.menu.MenuPreferenceFragment
import sgtmelon.scriptum.source.cases.screen.CloseScreenCase
import sgtmelon.scriptum.source.cases.screen.ContentScreenCase
import sgtmelon.scriptum.source.cases.screen.RotateScreenCase
import sgtmelon.scriptum.source.ui.tests.ParentUiRotationTest
import sgtmelon.scriptum.source.ui.tests.launchMain
import sgtmelon.scriptum.source.ui.tests.launchMenuPreference
import kotlin.random.Random

/**
 * Test for [MenuPreferenceFragment].
 */
@RunWith(AndroidJUnit4::class)
class MenuPreferenceTest : ParentUiRotationTest(),
    CloseScreenCase,
    ContentScreenCase,
    RotateScreenCase {

    @Test override fun closeScreen() = launchMain {
        openNotes(isEmpty = true) {
            openPreferences { clickClose() }
            assert(isEmpty = true)
        }
    }

    @Test override fun content() = launchMenuPreference({ setupRandomContent() }) {
        assert()
    }

    @Test fun openBackup() = launchMenuPreference { openBackup() }

    @Test fun openNotes() = launchMenuPreference { openNotes() }

    @Test fun openAlarm() = launchMenuPreference { openAlarm() }

    @Test fun openPrivacyPolicy() = launchMenuPreference { openPrivacyPolicy() }

    @Test fun openRate() = launchMenuPreference { openRate() }

    @Test override fun rotateScreen() = launchMenuPreference({ setupRandomContent() }) {
        rotate.switch()
        assert()
    }

    /** Setup random content for screen tests. */
    private fun setupRandomContent() {
        preferencesRepo.isDeveloper = Random.nextBoolean()
    }
}