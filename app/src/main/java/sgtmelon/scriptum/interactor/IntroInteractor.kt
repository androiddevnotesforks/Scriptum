package sgtmelon.scriptum.interactor

import sgtmelon.scriptum.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.presentation.screen.vm.IntroViewModel

/**
 * Interactor for [IntroViewModel].
 */
class IntroInteractor(private val preferenceRepo: IPreferenceRepo) : ParentInteractor(),
        IIntroInteractor {

    override fun onIntroFinish() {
        preferenceRepo.firstStart = false
    }

}