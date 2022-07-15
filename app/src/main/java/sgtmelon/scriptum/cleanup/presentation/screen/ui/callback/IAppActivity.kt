package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback

import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IAppViewModel

/**
 * Interface for communication [IAppViewModel] with [AppActivity].
 */
interface IAppActivity {

    fun setupTheme(@Theme theme: Int)

    fun changeControlColor()

    fun changeSystemColor()
}