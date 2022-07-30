package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.basic.extension.isDisplayed
import sgtmelon.scriptum.basic.extension.isEnabled
import sgtmelon.scriptum.basic.extension.swipeLeft
import sgtmelon.scriptum.basic.extension.swipeRight
import sgtmelon.scriptum.basic.extension.waitAfter
import sgtmelon.scriptum.cleanup.domain.model.data.IntroData
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroFragment
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Class for UI control of [IntroActivity], [IntroFragment].
 */
class IntroScreen : ParentUi() {

    //region Views

    private val viewPager = getViewById(R.id.intro_view_pager)
    private val pageIndicator = getViewById(R.id.intro_page_indicator)

    private fun getTitleText(p: Int) = getView(R.id.info_title_text, IntroData.title[p])
    private fun getDetailsText(p: Int) = getView(R.id.info_details_text, IntroData.details[p])

    private val endButton = getView(R.id.intro_end_button, R.string.info_intro_button)

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