package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.preference.IDevelopInteractor
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IDevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IDevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IDevelopFragment]
 */
class DevelopViewModel(application: Application) : ParentViewModel<IDevelopFragment>(application),
    IDevelopViewModel {

    // TODO add tests

    private lateinit var interactor: IDevelopInteractor

    fun setInteractor(interactor: IDevelopInteractor) {
        this.interactor = interactor
    }


    override fun onSetup(bundle: Bundle?) {
        callback?.setupPrints()
        callback?.setupScreens()
        callback?.setupOther()
    }

    // TODO

    override fun onClickScreenAlarm() {
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