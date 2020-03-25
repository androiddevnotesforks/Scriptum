package sgtmelon.scriptum.domain.interactor

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.screen.vm.impl.AppViewModel

/**
 * Interactor for [AppViewModel].
 */
class AppInteractor(private val preferenceRepo: IPreferenceRepo) : ParentInteractor(), IAppInteractor {

    @Theme override val theme: Int get() = preferenceRepo.theme

}