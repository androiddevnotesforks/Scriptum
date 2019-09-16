package sgtmelon.scriptum.screen.ui.callback

import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.vm.IntroViewModel

/**
 * Interface for communication [IntroViewModel] with [IntroActivity]
 */
interface IIntroActivity {

    fun setupViewPager()

    fun startMainActivity()

}