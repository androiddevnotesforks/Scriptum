package sgtmelon.scriptum.presentation.screen.ui.callback

import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel

/**
 * Interface for communication [IntroViewModel] with [IntroActivity]
 */
interface IIntroActivity {

    fun setupViewPager()

    fun startMainActivity()

}