package sgtmelon.scriptum.domain.interactor

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.presentation.screen.vm.impl.IntroViewModel

/**
 * Interactor for [IntroViewModel].
 */
class IntroInteractor(private val preferenceRepo: IPreferenceRepo) : ParentInteractor(),
        IIntroInteractor {

    override fun onIntroFinish() {
        preferenceRepo.firstStart = false
    }

}