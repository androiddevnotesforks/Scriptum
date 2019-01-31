package sgtmelon.scriptum.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import sgtmelon.scriptum.app.view.activity.IntroActivity
import sgtmelon.scriptum.office.annot.IntroAnn
import sgtmelon.scriptum.ui.screen.intro.IntroScreen

/**
 * Тест для [IntroActivity]
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class IntroTest : ParentTest() {

    @get:Rule
    var testRule = ActivityTestRule(IntroActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = true
    }

    override fun tearDown() {
        super.tearDown()

        prefUtils.firstStart = false
    }

    @Test
    fun test0_screenWork() {
        IntroScreen {
            Thread.sleep(1000)

            for (i in 0 until IntroAnn.count) {
                assert {
                    onDisplayTitle(IntroAnn.title[i])
                    onDisplayDetails(IntroAnn.details[i])
                }

                onSwipeNext()
                Thread.sleep(1000)
                assert { isEnableEndButton(i) }
            }

            for (i in IntroAnn.count - 2 downTo 0) {
                onSwipePrevious()
                Thread.sleep(1000)
                assert { isEnableEndButton(i) }
            }

            for (i in 0 until IntroAnn.count) {
                onSwipeNext()
                Thread.sleep(1000)
                assert { isEnableEndButton(i) }
            }

            assert { onDisplayEndButton() }

            onClickEndButton()
        }
    }

}