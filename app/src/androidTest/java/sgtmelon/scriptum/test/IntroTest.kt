package sgtmelon.scriptum.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.view.activity.IntroActivity
import sgtmelon.scriptum.ui.SCROLL
import sgtmelon.scriptum.ui.screen.intro.IntroScreen

/**
 * Тест для [IntroActivity]
 */
@RunWith(AndroidJUnit4::class)
class IntroTest : ParentTest() {

    @get:Rule val testRule = ActivityTestRule(IntroActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = true
    }

    override fun tearDown() {
        super.tearDown()

        prefUtils.firstStart = false
    }

    @Test fun testContent() {
        IntroScreen {
            for (position in 0 until count - 1) {
                assert { onDisplayContent(position) }
                onSwipe(SCROLL.END)
            }
        }
    }

    @Test fun testEndButton() {
        IntroScreen {
            for (position in 0 until count - 1) {
                assert { isEnableEndButton(position) }
                onSwipe(SCROLL.END)
            }

            assert { onDisplayEndButton() }

            for (position in count - 1 downTo 0) {
                assert { isEnableEndButton(position) }
                onSwipe(SCROLL.START)
            }

            for (position in 0 until count - 1) {
                assert { isEnableEndButton(position) }
                onSwipe(SCROLL.END)
            }

            assert { onDisplayEndButton() }

            onClickEndButton()
        }
    }

}