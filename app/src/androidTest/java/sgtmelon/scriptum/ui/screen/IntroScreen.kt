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

    fun assert(p: Int = 0, enabled: Boolean = false) = Assert(p, enabled)

    fun onPassThrough(scroll: Scroll) {
        (when (scroll) {
            Scroll.START -> count - 1 downTo 0
            Scroll.END -> 0 until count - 1
        }).forEach {
            assert(it, enabled = it == IntroData.count - 1)
            onSwipe(scroll)
        }

        when (scroll) {
            Scroll.START -> assert()
            Scroll.END -> assert(p = count - 1, enabled = true)
        }
    }

    private val count: Int get() = IntroData.count

    private fun onSwipe(scroll: Scroll) = action {
        when (scroll) {
            Scroll.START -> onSwipeRight(R.id.intro_pager)
            Scroll.END -> onSwipeLeft(R.id.intro_pager)
        }
    }

    fun onClickEndButton(func: MainScreen.() -> Unit = {}) {
        action { onClick(R.id.intro_end_button) }
        MainScreen.invoke(func)
    }

    companion object {
        operator fun invoke(func: IntroScreen.() -> Unit) = IntroScreen().apply {
            assert()
            func()
        }
    }

    class Assert(p: Int, enabled: Boolean) : BasicMatch() {

        init {
            onDisplay(R.id.intro_pager)
            onDisplay(R.id.intro_page_indicator)

            onDisplay(R.id.info_title_text, IntroData.title[p])
            onDisplay(R.id.info_details_text, IntroData.details[p])

            isEnabled(R.id.intro_end_button, enabled)

            if (enabled) {
                onDisplay(R.id.intro_end_button, R.string.info_intro_button)
            }
        }

    }

}