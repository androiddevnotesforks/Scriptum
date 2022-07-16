package sgtmelon.scriptum.cleanup.domain.interactor.impl

import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.cleanup.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IIntroViewModel

/**
 * Interactor for [IIntroViewModel].
 */
class IntroInteractor(private val preferences: Preferences) : ParentInteractor(),
    IIntroInteractor {

    override fun onIntroFinish() {
        preferences.isFirstStart = false
    }
}