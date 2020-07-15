package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.impl.AppInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel

/**
 * Interface for communication [IAppViewModel] with [AppInteractor].
 */
interface IAppInteractor {

    @Theme val theme: Int

}