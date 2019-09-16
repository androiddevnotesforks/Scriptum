package sgtmelon.scriptum.interactor.callback

import sgtmelon.scriptum.interactor.AppInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.vm.AppViewModel

/**
 * Interface for communication [AppViewModel] with [AppInteractor]
 */
interface IAppInteractor {

    @Theme val theme: Int

}