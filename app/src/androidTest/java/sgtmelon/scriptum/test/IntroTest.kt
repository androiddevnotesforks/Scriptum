package sgtmelon.scriptum.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.screen.view.intro.IntroActivity
import sgtmelon.scriptum.ui.screen.SplashScreen

/**
 * Тест для [IntroActivity]
 *
 * @author SerjantArbuz
 */
@RunWith(AndroidJUnit4::class)
class IntroTest : ParentTest() {

    override fun setUp() {
        super.setUp()

        beforeLaunch {
            preference.firstStart = true
            testData.clear()
        }
    }

    @Test fun contentPlacement() {
        SplashScreen { introScreen { onPassThrough(Scroll.END) } }
    }

    @Test fun endButtonEnabled() {
        SplashScreen {
            introScreen {
                onPassThrough(Scroll.END)
                assert { onDisplayEndButton() }

                onPassThrough(Scroll.START)

                onPassThrough(Scroll.END)
                assert { onDisplayEndButton() }
            }
        }
    }

    @Test fun endButtonWork() {
        SplashScreen {
            introScreen {
                repeat(times = count - 1) { onSwipe(Scroll.END) }

                assert { onDisplayEndButton() }
                onClickEndButton()
            }

            mainScreen()
        }
    }

}