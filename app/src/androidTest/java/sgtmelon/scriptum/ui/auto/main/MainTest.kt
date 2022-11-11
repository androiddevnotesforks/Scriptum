package sgtmelon.scriptum.ui.auto.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.infrastructure.model.key.MainPage
import sgtmelon.scriptum.infrastructure.screen.main.MainActivity
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

/**
 * Test for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainTest : ParentUiTest() {

    @Test fun correctPage() = launch {
        mainScreen {
            repeat(times = 3) { for (page in MainPage.values()) openPage(page, isEmpty = true) }
        }
    }
}