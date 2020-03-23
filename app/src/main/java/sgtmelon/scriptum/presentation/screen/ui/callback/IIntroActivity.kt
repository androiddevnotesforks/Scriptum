package sgtmelon.scriptum.presentation.screen.ui.callback

import sgtmelon.scriptum.presentation.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.vm.IntroViewModel

/**
 * Interface for communication [IntroViewModel] with [IntroActivity]
 */
interface IIntroActivity {

    fun setupViewPager()

    fun startMainActivity()

}