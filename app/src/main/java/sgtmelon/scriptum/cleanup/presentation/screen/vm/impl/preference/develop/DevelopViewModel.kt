package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.common.utils.runBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IDevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IDevelopViewModel
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.ParentViewModel
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor

/**
 * ViewModel for [IDevelopFragment].
 */
class DevelopViewModel(
    callback: IDevelopFragment,
    private val interactor: DevelopInteractor
) : ParentViewModel<IDevelopFragment>(callback),
    IDevelopViewModel {

    override fun onSetup(bundle: Bundle?) {
        callback?.setupPrints()
        callback?.setupScreens()
        callback?.setupService()
        callback?.setupOther()
    }


    override fun onClickPrint(type: PrintType) {
        callback?.openPrintScreen(type)
    }

    override fun onClickAlarm() {
        viewModelScope.launch {
            val noteId = runBack { interactor.getRandomNoteId() }
            callback?.openAlarmScreen(noteId)
        }
    }

    override fun onClickReset() {
        interactor.resetPreferences()
        callback?.showToast(R.string.pref_toast_develop_clear)
    }
}