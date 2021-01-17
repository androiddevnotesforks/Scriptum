package sgtmelon.scriptum.domain.interactor.impl

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.presentation.screen.vm.callback.IIntroViewModel

/**
 * Interactor for [IIntroViewModel].
 */
class IntroInteractor(private val preferenceRepo: IPreferenceRepo) : ParentInteractor(),
    IIntroInteractor {

    override fun onIntroFinish() {
        preferenceRepo.firstStart = false
    }
}