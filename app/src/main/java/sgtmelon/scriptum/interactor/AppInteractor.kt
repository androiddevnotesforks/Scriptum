package sgtmelon.scriptum.interactor

import sgtmelon.scriptum.interactor.callback.IAppInteractor
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.screen.vm.AppViewModel

/**
 * Interactor for [AppViewModel].
 */
class AppInteractor(private val iPreferenceRepo: IPreferenceRepo) : ParentInteractor(), IAppInteractor {

    @Theme override val theme: Int get() = iPreferenceRepo.theme

}