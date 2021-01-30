package sgtmelon.scriptum.presentation.screen.vm.impl

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.presentation.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IIntroViewModel

/**
 * ViewModel for [IIntroActivity].
 */
class IntroViewModel(application: Application) : ParentViewModel<IIntroActivity>(application),
        IIntroViewModel {

    private lateinit var interactor: IIntroInteractor

    fun setInteractor(interactor: IIntroInteractor) {
        this.interactor = interactor
    }


    override fun onSetup(bundle: Bundle?) {
        val isLastPage = bundle?.getBoolean(IS_LAST_PAGE, ND_LAST_PAGE) ?: ND_LAST_PAGE

        callback?.setupViewPager(isLastPage)
        callback?.setupInsets()
    }

    override fun onSaveData(bundle: Bundle) {
        val currentPosition = callback?.getCurrentPosition() ?: return
        val itemCount = callback?.getItemCount() ?: return

        bundle.putBoolean(IS_LAST_PAGE, currentPosition == itemCount - 1)
    }

    override fun onClickEnd() {
        interactor.onIntroFinish()
        callback?.openMainScreen()
    }

    companion object {
        @RunPrivate const val IS_LAST_PAGE = "INTRO_IS_LAST_PAGE"
        @RunPrivate const val ND_LAST_PAGE = false
    }
}