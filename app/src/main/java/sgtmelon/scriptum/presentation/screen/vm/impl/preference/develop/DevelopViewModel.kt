package sgtmelon.scriptum.presentation.screen.vm.impl.preference.develop

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.interactor.callback.preference.IDevelopInteractor
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.extension.runBack
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.develop.IDevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IDevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IDevelopFragment].
 */
class DevelopViewModel(application: Application) : ParentViewModel<IDevelopFragment>(application),
    IDevelopViewModel {

    private lateinit var interactor: IDevelopInteractor

    fun setInteractor(interactor: IDevelopInteractor) {
        this.interactor = interactor
    }


    override fun onSetup(bundle: Bundle?) {
        callback?.setupPrints()
        callback?.setupScreens()
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