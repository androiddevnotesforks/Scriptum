package sgtmelon.scriptum.cleanup.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.waitAfter
import sgtmelon.scriptum.cleanup.domain.model.data.IntroData
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroFragment
import sgtmelon.scriptum.cleanup.testData.Scroll
import sgtmelon.scriptum.cleanup.ui.ParentUi
import sgtmelon.scriptum.cleanup.ui.screen.main.MainScreen
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.swipeLeft
import sgtmelon.test.cappuccino.utils.swipeRight

/**
 * Class for UI control of [IntroActivity], [IntroFragment].
 */
class IntroScreen : ParentUi() {

    //region Views

    private val viewPager = getViewById(R.id.view_pager)
    private val pageIndicator = getViewById(R.id.page_indicator)

    private fun getTitleText(p: Int) = getView(R.id.title_text, IntroData.title[p])
    private fun getDetailsText(p: Int) = getView(R.id.details_text, IntroData.details[p])

    private val endButton = getView(R.id.end_button, R.string.info_intro_button)

    //endregion

    fun onPassThrough(scroll: Scroll) {
        val count = IntroData.count

        for (it in when (scroll) {
            Scroll.START -> count - 1 downTo 0
            Scroll.END -> 0 until count - 1
        }) {
            assert(it, isEnabled = it == count - 1)
            onSwipe(scroll)
        }

        when (scroll) {
            Scroll.START -> assert()
            Scroll.END -> assert(p = count - 1, isEnabled = true)
        }
    }

    fun onSwipe(scroll: Scroll) = waitAfter(SWIPE_TIME) {
        when (scroll) {
            Scroll.START -> viewPager.swipeRight()
            Scroll.END -> viewPager.swipeLeft()
        }
    }

    fun onClickEndButton(func: MainScreen.() -> Unit = {}) {
        endButton.click()
        MainScreen(func)
    }


    fun assert(p: Int = 0, isEnabled: Boolean = false) = apply {
        viewPager.isDisplayed()
        pageIndicator.isDisplayed()

        getTitleText(p).isDisplayed()
        getDetailsText(p).isDisplayed()

        endButton.isEnabled(isEnabled).apply { if (isEnabled) isDisplayed() }
    }

    companion object {
        const val SWIPE_TIME = 150L

        operator fun invoke(func: IntroScreen.() -> Unit) = IntroScreen().assert().apply(func)
    }
}