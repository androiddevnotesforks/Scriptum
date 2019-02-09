package sgtmelon.scriptum.test.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.view.activity.SplashActivity
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen
import sgtmelon.scriptum.ui.screen.main.PAGE

@RunWith(AndroidJUnit4::class)
class MainTest : ParentTest() {

    private val listPage: List<PAGE> = object : ArrayList<PAGE>() {
        init {
            add(PAGE.RANK)
            add(PAGE.NOTES)
            add(PAGE.BIN)
            add(PAGE.RANK)
            add(PAGE.BIN)
            add(PAGE.NOTES)
            add(PAGE.RANK)
        }
    }

    @get:Rule val testRule = ActivityTestRule(SplashActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun testNavigation() {
        MainScreen {
            assert { onDisplayContent(PAGE.NOTES) }

            for (page in listPage) {
                navigateTo(page)
                assert { onDisplayContent(page) }
            }
        }
    }

    @Test fun testDisplayInfo() {
        MainScreen {
            assert { onDisplayContent(PAGE.NOTES) }

            for (page in listPage) {
                navigateTo(page)
                assert { onDisplayContent(page) }
            }
        }
    }

}