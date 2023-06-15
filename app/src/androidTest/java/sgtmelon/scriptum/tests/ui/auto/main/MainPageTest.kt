package sgtmelon.scriptum.tests.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.source.ui.tests.ParentUiTest
import sgtmelon.scriptum.source.ui.tests.launchMain

/**
 * Test of navigation menu pages switch in [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainPageTest : ParentUiTest() {

    @Test fun correctPage() = launchMain {
        repeat(times = 3) { for (page in MainPage.values()) openPage(page, isEmpty = true) }
    }
}