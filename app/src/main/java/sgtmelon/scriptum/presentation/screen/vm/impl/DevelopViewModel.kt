package sgtmelon.scriptum.presentation.screen.vm.impl

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.data.repository.room.DevelopRepo
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.presentation.screen.ui.callback.IDevelopActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.DevelopActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IDevelopViewModel

/**
 * ViewModel for [DevelopActivity].
 */
class DevelopViewModel(application: Application) : ParentViewModel<IDevelopActivity>(application),
        IDevelopViewModel {

    private val developRepo: IDevelopRepo = DevelopRepo(context)

    override fun onSetup(bundle: Bundle?) {
        viewModelScope.launch {
            callback?.apply {
                fillAboutNoteTable(developRepo.getNoteTablePrint())
                fillAboutRollTable(developRepo.getRollTablePrint())
                fillAboutRankTable(developRepo.getRankTablePrint())
                fillAboutPreference(developRepo.getPreferencePrint())
            }
        }
    }

    override fun onIntroClick() {
        callback?.startIntroActivity()
    }

}