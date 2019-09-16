package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.interactor.IntroInteractor
import sgtmelon.scriptum.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.vm.callback.IIntroViewModel

/**
 * ViewModel for [IntroActivity]
 */
class IntroViewModel(application: Application) : ParentViewModel<IIntroActivity>(application),
        IIntroViewModel {

    private val iInteractor: IIntroInteractor by lazy { IntroInteractor(context) }

    override fun onSetup(bundle: Bundle?) {
        callback?.setupViewPager()
    }

    override fun onClickEnd() {
        iInteractor.onIntroFinish()
        callback?.startMainActivity()
    }

}