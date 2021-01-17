package sgtmelon.scriptum.domain.interactor.impl

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.vm.callback.IAppViewModel

/**
 * Interactor for [IAppViewModel].
 */
class AppInteractor(
    private val preferenceRepo: IPreferenceRepo
) : ParentInteractor(),
    IAppInteractor {

    @Theme override val theme: Int get() = preferenceRepo.theme

}