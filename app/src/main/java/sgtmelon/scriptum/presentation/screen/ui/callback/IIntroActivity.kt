package sgtmelon.scriptum.presentation.screen.ui.callback

import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IIntroViewModel

/**
 * Interface for communication [IIntroViewModel] with [IntroActivity].
 */
interface IIntroActivity {

    fun setupViewPager(isLastPage: Boolean)

    fun setupInsets()

    fun getCurrentPosition(): Int

    fun getItemCount(): Int

    fun openMainScreen()
}