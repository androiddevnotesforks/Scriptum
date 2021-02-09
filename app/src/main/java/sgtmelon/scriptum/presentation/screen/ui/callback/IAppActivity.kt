package sgtmelon.scriptum.presentation.screen.ui.callback

import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel

/**
 * Interface for communication [IAppViewModel] with [AppActivity].
 */
interface IAppActivity {

    fun setupTheme(@Theme theme: Int)

    fun changeControlColor()

    fun changeSystemColor()
}