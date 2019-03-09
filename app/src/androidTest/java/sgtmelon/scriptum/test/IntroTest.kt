package sgtmelon.scriptum.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.app.screen.intro.IntroActivity
import sgtmelon.scriptum.data.Scroll
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
                onSwipe(Scroll.END)
            }
        }
    }

    @Test fun testEndButton() {
        IntroScreen {
            for (position in 0 until count - 1) {
                assert { isEnableEndButton(position) }
                onSwipe(Scroll.END)
            }

            assert { onDisplayEndButton() }

            for (position in count - 1 downTo 0) {
                assert { isEnableEndButton(position) }
                onSwipe(Scroll.START)
            }

            for (position in 0 until count - 1) {
                assert { isEnableEndButton(position) }
                onSwipe(Scroll.END)
            }

            assert { onDisplayEndButton() }

            onClickEndButton()
        }
    }

}