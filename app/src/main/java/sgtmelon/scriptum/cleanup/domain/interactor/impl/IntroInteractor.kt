package sgtmelon.scriptum.cleanup.domain.interactor.impl

import sgtmelon.scriptum.infrastructure.preferences.AppPreferences
import sgtmelon.scriptum.cleanup.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IIntroViewModel

/**
 * Interactor for [IIntroViewModel].
 */
class IntroInteractor(private val preferenceRepo: AppPreferences) : ParentInteractor(),
    IIntroInteractor {

    override fun onIntroFinish() {
        preferenceRepo.firstStart = false
    }
}