package sgtmelon.scriptum.ui.screen.intro

import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.data.IntroData
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.Scroll

class IntroScreen: ParentUi(){

    companion object {
        operator fun invoke(func: IntroScreen.() -> Unit) = IntroScreen().apply { func() }
    }

    fun assert(func: IntroAssert.() -> Unit) = IntroAssert().apply { func() }

    val count: Int get() = IntroData.count

    fun onSwipe(scroll: Scroll) = action {
        when(scroll) {
            Scroll.START -> onSwipeRight(R.id.intro_pager)
            Scroll.END -> onSwipeLeft(R.id.intro_pager)
        }
    }

    fun onClickEndButton() = action { onClick(R.id.intro_end_button) }

}