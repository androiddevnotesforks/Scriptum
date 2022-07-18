package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback

import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.AppActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IAppViewModel
import sgtmelon.scriptum.infrastructure.model.key.Theme

/**
 * Interface for communication [IAppViewModel] with [AppActivity].
 */
interface IAppActivity {

    fun setupTheme(theme: Theme)

    fun changeControlColor()

    fun changeSystemColor()
}