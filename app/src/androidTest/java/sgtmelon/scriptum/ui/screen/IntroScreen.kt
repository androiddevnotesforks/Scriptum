package sgtmelon.scriptum.ui.screen

import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.data.IntroData
import sgtmelon.scriptum.data.Scroll
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class IntroScreen: ParentUi(){

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    val count: Int get() = IntroData.count

    fun onSwipe(scroll: Scroll) = action {
        when(scroll) {
            Scroll.START -> onSwipeRight(R.id.intro_pager)
            Scroll.END -> onSwipeLeft(R.id.intro_pager)
        }
    }

    fun onClickEndButton() = action { onClick(R.id.intro_end_button) }

    companion object {
        operator fun invoke(func: IntroScreen.() -> Unit) = IntroScreen().apply { func() }
    }

    class Assert : BasicMatch() {

        fun onDisplayContent(position: Int) {
            onDisplay(R.id.info_title_text, IntroData.title[position])
            onDisplay(R.id.info_details_text, IntroData.details[position])
        }

        fun isEnableEndButton(position: Int) {
            with (position == IntroData.count -1) {
                if (this) Thread.sleep(100)

                isEnable(R.id.intro_end_button, enable = this)
            }
        }

        fun onDisplayEndButton() = onDisplay(R.id.intro_end_button, R.string.info_intro_button)

    }

}