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

    @get:Rule val testRule = ActivityTestRule(IntroActivity::class.java)

    override fun setUp() {
        super.setUp()

        prefUtils.firstStart = true
    }

    override fun tearDown() {
        super.tearDown()

        prefUtils.firstStart = false
    }

    @Test fun testContentAndScroll() {
        IntroScreen {
            for (i in 0 until IntroAnn.count - 1) {
                assert {
                    onDisplayTitle(IntroAnn.title[i])
                    onDisplayDetails(IntroAnn.details[i])
                }

                assert { isEnableEndButton(i) }
                onSwipeNext()
            }

            for (i in IntroAnn.count - 1 downTo 0) {
                assert { isEnableEndButton(i) }
                onSwipePrevious()
            }

            for (i in 0 until IntroAnn.count - 1) {
                assert { isEnableEndButton(i) }
                onSwipeNext()
            }

            assert { onDisplayEndButton() }
            onClickEndButton()
        }
    }

}