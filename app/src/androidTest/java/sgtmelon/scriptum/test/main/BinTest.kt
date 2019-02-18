package sgtmelon.scriptum.test.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.ui.splash.SplashActivity
import sgtmelon.scriptum.test.ParentTest
import sgtmelon.scriptum.ui.screen.main.MainScreen

@RunWith(AndroidJUnit4::class)
class BinTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(SplashActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false
    }

    @Test fun testClearBin() {
        db.clearAllTables()

        MainScreen {
            assert { onDisplayContent() }

            addDialog {
                open()
                assert { onDisplayContent() }
            }
        }
    }

    @Test fun testNoteDialog() {

    }

}