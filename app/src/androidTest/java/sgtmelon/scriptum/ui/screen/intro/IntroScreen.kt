package sgtmelon.scriptum.ui.screen.intro

import sgtmelon.scriptum.R
import sgtmelon.scriptum.office.annot.IntroAnn
import sgtmelon.scriptum.ui.SCROLL
import sgtmelon.scriptum.ui.screen.ParentScreen

class IntroScreen: ParentScreen(){

    companion object {
        operator fun invoke(func: IntroScreen.() -> Unit) = IntroScreen().apply { func() }
    }

    fun assert(func: IntroAssert.() -> Unit) = IntroAssert().apply { func() }

    val count: Int get() = IntroAnn.count

    fun onSwipe(scroll: SCROLL) = action {
        when(scroll) {
            SCROLL.START -> onSwipeRight(R.id.intro_pager)
            SCROLL.END -> onSwipeLeft(R.id.intro_pager)
        }
    }

    fun onClickEndButton() = action { onClick(R.id.info_end_button) }

}