package sgtmelon.scriptum.cleanup.domain.interactor.callback

import sgtmelon.scriptum.cleanup.domain.interactor.impl.AppInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Theme
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IAppViewModel

/**
 * Interface for communication [IAppViewModel] with [AppInteractor].
 */
interface IAppInteractor {

    @Theme val theme: Int

}