package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.data.IntroData
import sgtmelon.scriptum.screen.view.intro.IntroActivity
import sgtmelon.scriptum.screen.view.intro.IntroFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch
import sgtmelon.scriptum.ui.screen.main.MainScreen

/**
 * Класс для ui контроля экрана [IntroActivity], [IntroFragment]
 *
 * @author SerjantArbuz
 */
class IntroScreen : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    val count: Int get() = IntroData.count

    fun onSwipe(scroll: Scroll) = action {
        when (scroll) {
            Scroll.START -> onSwipeRight(R.id.intro_pager)
            Scroll.END -> onSwipeLeft(R.id.intro_pager)
        }
    }

    fun onClickEndButton(func: MainScreen.() -> Unit = {}) {
        action { onClick(R.id.intro_end_button) }
        MainScreen.invoke(func)
    }

    fun onPassThrough(scroll: Scroll) = assert {
        (when (scroll) {
            Scroll.START -> count - 1 downTo 0
            Scroll.END -> 0 until count - 1
        }).forEach {
            onDisplayContent(it, enabled = it == IntroData.count - 1)
            onSwipe(scroll)
        }

        when (scroll) {
            Scroll.START -> onDisplayContent(p = 0, enabled = false)
            Scroll.END -> onDisplayContent(p = count - 1, enabled = true)
        }
    }

    companion object {
        operator fun invoke(func: IntroScreen.() -> Unit) = IntroScreen().apply {
            assert { onDisplayContent(p = 0, enabled = false) }
            func()
        }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(p: Int, enabled: Boolean) {
            onDisplay(R.id.intro_pager)
            onDisplay(R.id.intro_page_indicator)

            onDisplay(R.id.info_title_text, IntroData.title[p])
            onDisplay(R.id.info_details_text, IntroData.details[p])

            isEnabled(R.id.intro_end_button, enabled)
            if (enabled) onDisplay(R.id.intro_end_button, R.string.info_intro_button)
        }

    }

}