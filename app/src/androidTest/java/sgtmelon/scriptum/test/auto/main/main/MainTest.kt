package sgtmelon.scriptum.test.auto.main.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.key.MainPage
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.test.ParentUiTest

/**
 * Test for [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class MainTest : ParentUiTest() {

    @Test fun startScreen() = launch { mainScreen() }

    @Test fun menuClickCorrectScreen() = launch {
        mainScreen { repeat(times = 3) { pageList.forEach { openPage(it, empty = true) } } }
    }

    @Test fun addFabVisible() = launch {
        mainScreen { repeat(times = 3) { pageList.forEach { onNavigateTo(it) } } }
    }


    private companion object {
        val pageList = arrayListOf(
                MainPage.RANK, MainPage.NOTES, MainPage.BIN,
                MainPage.RANK, MainPage.BIN, MainPage.NOTES
        )
    }

}