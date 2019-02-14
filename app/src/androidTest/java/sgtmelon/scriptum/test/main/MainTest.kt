package sgtmelon.scriptum.test.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.view.activity.SplashActivity
import sgtmelon.scriptum.office.annot.key.MainPage
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

@RunWith(AndroidJUnit4::class)
class MainTest : ParentTest() {

    private val listPage = object : ArrayList<MainPage.Name>() {
        init {
            add(MainPage.Name.RANK)
            add(MainPage.Name.NOTES)
            add(MainPage.Name.BIN)
            add(MainPage.Name.RANK)
            add(MainPage.Name.BIN)
            add(MainPage.Name.NOTES)
        }
    }

    @get:Rule val testRule = ActivityTestRule(SplashActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun testNavigation() {
        MainScreen {
            assert { onDisplayContent() }

            repeat(times = 3) {
                for (page in listPage) {
                    navigateTo(page)
                    assert { onDisplayContent(page) }
                }
            }
        }
    }

    @Test fun testDisplayInfo() {
        MainScreen {
            assert { onDisplayContent() }

            repeat(times = 3) {
                for (page in listPage) {
                    navigateTo(page)
                    assert { onDisplayContent(page) }
                }
            }
        }
    }

}