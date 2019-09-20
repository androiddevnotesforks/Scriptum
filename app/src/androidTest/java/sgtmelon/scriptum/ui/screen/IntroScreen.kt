package sgtmelon.scriptum.ui.screen

import android.view.View
import org.hamcrest.Matcher
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.data.IntroData
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.ui.intro.IntroFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.*
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Class for UI control of [IntroActivity], [IntroFragment]
 */
class IntroScreen : ParentUi() {

    //region Views

    private val viewPager = getViewById(R.id.intro_pager)
    private val pageIndicator = getViewById(R.id.intro_page_indicator)

    private fun getTitleText(p: Int): Matcher<View> {
        return getViewById(R.id.info_title_text).withText(IntroData.title[p])
    }

    private fun getDetailsText(p: Int): Matcher<View> {
        return getViewById(R.id.info_details_text).withText(IntroData.details[p])
    }

    private val endButton = getViewById(R.id.intro_end_button).withText(R.string.info_intro_button)

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

    private fun onSwipe(scroll: Scroll) = when (scroll) {
        Scroll.START -> viewPager.swipeRight()
        Scroll.END -> viewPager.swipeLeft()
    }

    fun onClickEndButton(func: MainScreen.() -> Unit = {}) {
        endButton.click()
        MainScreen.invoke(func)
    }


    fun assert(p: Int = 0, enabled: Boolean = false) {
        viewPager.isDisplayed()
        pageIndicator.isDisplayed()

        getTitleText(p).isDisplayed()
        getDetailsText(p).isDisplayed()

        endButton.isEnabled(enabled).apply { if (enabled) isDisplayed() }
    }

    companion object {
        operator fun invoke(func: IntroScreen.() -> Unit) =
                IntroScreen().apply { assert() }.apply(func)
    }

}