package sgtmelon.scriptum.interactor.callback

import sgtmelon.scriptum.interactor.AppInteractor
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Interface for communicate with [AppInteractor]
 */
interface IAppInteractor {

    @Theme val theme: Int

}