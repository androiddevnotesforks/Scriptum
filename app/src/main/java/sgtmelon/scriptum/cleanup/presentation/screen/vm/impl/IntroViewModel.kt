package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl

import android.os.Bundle
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IIntroViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

/**
 * ViewModel for [IIntroActivity].
 */
class IntroViewModel(
    callback: IIntroActivity,
    private val preferencesRepo: PreferencesRepo
) : ParentViewModel<IIntroActivity>(callback),
        IIntroViewModel {

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
        preferencesRepo.isFirstStart = false
        callback?.openMainScreen()
    }

    companion object {
        @RunPrivate const val IS_LAST_PAGE = "INTRO_IS_LAST_PAGE"
        @RunPrivate const val ND_LAST_PAGE = false
    }
}