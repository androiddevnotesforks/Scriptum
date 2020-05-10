package sgtmelon.scriptum.presentation.screen.vm.impl

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.domain.interactor.callback.IDevelopInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.IDevelopActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.DevelopActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IDevelopViewModel

/**
 * ViewModel for [DevelopActivity].
 */
class DevelopViewModel(application: Application) : ParentViewModel<IDevelopActivity>(application),
        IDevelopViewModel {

    private lateinit var interactor: IDevelopInteractor

    fun setInteractor(interactor: IDevelopInteractor) {
        this.interactor = interactor
    }


    override fun onSetup(bundle: Bundle?) {
        viewModelScope.launch {
            callback?.apply {
                fillAboutNoteTable(interactor.getNoteTablePrint())
                fillAboutRollTable(interactor.getRollTablePrint())
                fillAboutRankTable(interactor.getRankTablePrint())
                fillAboutPreference(interactor.getPreferencePrint())
            }
        }
    }

    override fun onIntroClick() {
        callback?.startIntroActivity()
    }

}