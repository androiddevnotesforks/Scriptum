package sgtmelon.scriptum.cleanup.domain.interactor.impl

import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.cleanup.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IIntroViewModel

/**
 * Interactor for [IIntroViewModel].
 */
class IntroInteractor(private val preferenceRepo: Preferences) : ParentInteractor(),
    IIntroInteractor {

    override fun onIntroFinish() {
        preferenceRepo.isFirstStart = false
    }
}