package sgtmelon.scriptum.cleanup.test.ui.auto.screen.main.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.cleanup.domain.model.key.MainPage
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.cleanup.test.parent.ParentUiTest

/**
 * Test for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainTest : ParentUiTest() {

    private val pageList = arrayListOf(
        MainPage.RANK, MainPage.NOTES, MainPage.BIN,
        MainPage.RANK, MainPage.BIN, MainPage.NOTES
    )

    @Test fun startScreen() = launch { mainScreen() }

    @Test fun menuClickCorrectScreen() = launch {
        mainScreen { repeat(times = 3) { for (page in pageList) openPage(page, isEmpty = true) } }
    }

    @Test fun addFabVisible() = launch {
        mainScreen { repeat(times = 3) { for (page in pageList) onNavigateTo(page) } }
    }
}