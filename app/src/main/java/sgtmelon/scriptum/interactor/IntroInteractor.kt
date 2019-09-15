package sgtmelon.scriptum.interactor

import android.content.Context
import sgtmelon.scriptum.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.screen.vm.IntroViewModel

/**
 * Interactor for [IntroViewModel]
 */
class IntroInteractor(context: Context) : ParentInteractor(context), IIntroInteractor {

    override fun onIntroFinish() {
        iPreferenceRepo.firstStart = false
    }

}