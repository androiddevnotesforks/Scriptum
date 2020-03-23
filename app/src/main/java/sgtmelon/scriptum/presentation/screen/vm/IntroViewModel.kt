package sgtmelon.scriptum.presentation.screen.vm

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.presentation.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IIntroViewModel

/**
 * ViewModel for [IntroActivity]
 */
class IntroViewModel(application: Application) : ParentViewModel<IIntroActivity>(application),
        IIntroViewModel {

    private lateinit var interactor: IIntroInteractor

    fun setInteractor(interactor: IIntroInteractor) {
        this.interactor = interactor
    }


    override fun onSetup(bundle: Bundle?) {
        callback?.setupViewPager()
    }

    override fun onClickEnd() {
        interactor.onIntroFinish()
        callback?.startMainActivity()
    }

}