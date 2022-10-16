package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.preference.develop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extensions.runBack
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop.IDevelopFragment
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.DevelopViewModel
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor

/**
 * ViewModel for [IDevelopFragment].
 */
class DevelopViewModelImpl(
    private val callback: IDevelopFragment,
    private val interactor: DevelopInteractor
) : ViewModel(),
    DevelopViewModel {

    override fun onClickAlarm() {
        viewModelScope.launch {
            val noteId = runBack { interactor.getRandomNoteId() }
            callback.openAlarmScreen(noteId)
        }
    }

    override fun onClickReset() {
        interactor.resetPreferences()
        callback.showToast(R.string.pref_toast_develop_clear)
    }
}