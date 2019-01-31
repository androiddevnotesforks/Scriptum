package sgtmelon.scriptum.ui.screen.intro

import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.BasicAction
import sgtmelon.scriptum.ui.screen.ParentScreen

class IntroScreen: ParentScreen(){

    companion object {
        operator fun invoke(func: IntroScreen.() -> Unit) = IntroScreen().apply { func() }
    }

    fun assert(func: IntroAssert.() -> Unit) = IntroAssert().apply { func() }

    fun onSwipeNext() = action { onSwipeLeft(R.id.intro_pager) }

    fun onSwipePrevious() = action { onSwipeRight(R.id.intro_pager) }

    fun onClickEndButton() = action { onClick(R.id.end_button) }

}