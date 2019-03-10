package sgtmelon.scriptum.test.main

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.model.key.MainPage
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

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = false

        db.apply { clearAllTables() }.close()
        testRule.launchActivity(Intent())
    }

    @Test fun testNavigation() {
        MainScreen {
            assert {
                onDisplayContent()
                onDisplayContent(MainPage.Name.NOTES)
            }

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
            assert {
                onDisplayContent()
                onDisplayContent(MainPage.Name.NOTES)
            }

            repeat(times = 3) {
                for (page in listPage) {
                    navigateTo(page)
                    assert { onDisplayContent(page) }
                }
            }
        }
    }


}