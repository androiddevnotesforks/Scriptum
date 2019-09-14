package sgtmelon.scriptum.interactor.app

import sgtmelon.scriptum.model.annotation.Theme

/**
 * Interface for communicate with [AppInteractor]
 */
interface IAppInteractor {

    @Theme val theme: Int

}