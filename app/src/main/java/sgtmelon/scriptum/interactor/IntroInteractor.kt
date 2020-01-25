package sgtmelon.scriptum.interactor

import sgtmelon.scriptum.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.screen.vm.IntroViewModel

/**
 * Interactor for [IntroViewModel].
 */
class IntroInteractor(private val iPreferenceRepo: IPreferenceRepo) : ParentInteractor(), IIntroInteractor {

    override fun onIntroFinish() {
        iPreferenceRepo.firstStart = false
    }

}