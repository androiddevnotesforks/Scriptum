package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.model.data.IntroData
import sgtmelon.scriptum.screen.view.intro.IntroActivity
import sgtmelon.scriptum.screen.view.intro.IntroFragment
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

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

    fun onClickEndButton() = action { onClick(R.id.intro_end_button) }

    fun passThrough(scroll: Scroll) = (when (scroll) {
        Scroll.START -> count - 1 downTo 0
        Scroll.END -> 0 until count - 1
    }).forEach {
        assert { isEnableEndButton(it) }
        onSwipe(scroll)
    }

    companion object {
        operator fun invoke(func: IntroScreen.() -> Unit) = IntroScreen().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent() {
            onDisplay(R.id.intro_pager)
            onDisplay(R.id.intro_page_indicator)
        }

        fun onDisplayContent(position: Int) {
            onDisplay(R.id.info_title_text, IntroData.title[position])
            onDisplay(R.id.info_details_text, IntroData.details[position])
        }

        fun isEnableEndButton(position: Int) =
                isEnabled(R.id.intro_end_button, enabled = position == IntroData.count - 1)

        fun onDisplayEndButton() = onDisplay(R.id.intro_end_button, R.string.info_intro_button)

    }

}