package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.impl.AppInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.vm.impl.AppViewModel

/**
 * Interface for communication [AppViewModel] with [AppInteractor]
 */
interface IAppInteractor {

    @Theme val theme: Int?

}