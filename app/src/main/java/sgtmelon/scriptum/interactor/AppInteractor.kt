package sgtmelon.scriptum.interactor

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.IAppInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.vm.AppViewModel

/**
 * Interactor for [AppViewModel]
 */
class AppInteractor(context: Context) : ParentInteractor(context), IAppInteractor {

    @Theme override val theme: Int get() = iPreferenceRepo.theme

}