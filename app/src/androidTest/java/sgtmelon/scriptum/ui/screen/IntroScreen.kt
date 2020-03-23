package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.data.IntroData
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Class for UI control of [IntroActivity], [IntroFragment].
 */
class IntroScreen : ParentUi() {

    //region Views

    private val viewPager = getViewById(R.id.intro_pager)
    private val pageIndicator = getViewById(R.id.intro_page_indicator)

    private fun getTitleText(p: Int) = getView(R.id.info_title_text, IntroData.title[p])
    private fun getDetailsText(p: Int) = getView(R.id.info_details_text, IntroData.details[p])

    private val endButton = getView(R.id.intro_end_button, R.string.info_intro_button)

    //endregion

    fun onPassThrough(scroll: Scroll) {
        val count = IntroData.count

        (when (scroll) {
            Scroll.START -> count - 1 downTo 0
            Scroll.END -> 0 until count - 1
        }).forEach {
            assert(it, enabled = it == count - 1)
            onSwipe(scroll)
        }

        when (scroll) {
            Scroll.START -> assert()
            Scroll.END -> assert(p = count - 1, enabled = true)
        }
    }

    private fun onSwipe(scroll: Scroll) = waitAfter(SWIPE_TIME) {
        when (scroll) {
            Scroll.START -> viewPager.swipeRight()
            Scroll.END -> viewPager.swipeLeft()
        }
    }

    fun onClickEndButton(func: MainScreen.() -> Unit = {}) {
        endButton.click()
        MainScreen(func)
    }


    fun assert(p: Int = 0, enabled: Boolean = false) = apply {
        viewPager.isDisplayed()
        pageIndicator.isDisplayed()

        getTitleText(p).isDisplayed()
        getDetailsText(p).isDisplayed()

        endButton.isEnabled(enabled).apply { if (enabled) isDisplayed() }
    }

    companion object {
        const val SWIPE_TIME = 150L

        operator fun invoke(func: IntroScreen.() -> Unit) = IntroScreen().assert().apply(func)
    }

}