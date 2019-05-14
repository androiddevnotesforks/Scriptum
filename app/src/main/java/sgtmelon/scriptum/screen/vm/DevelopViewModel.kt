package sgtmelon.scriptum.screen.vm

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.repository.DevelopRepo
import sgtmelon.scriptum.screen.callback.DevelopCallback
import sgtmelon.scriptum.screen.view.DevelopActivity

/**
 * ViewModel для [DevelopActivity]
 *
 * @author SerjantArbuz
 */
class DevelopViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: DevelopCallback

    private val iDevelopRepo = DevelopRepo.getInstance(context)

    fun onSetup() = viewModelScope.launch {
        callback.apply {
            fillAboutNoteTable(iDevelopRepo.getNoteTableData())
            fillAboutRollTable(iDevelopRepo.getRollTableData())
            fillAboutRankTable(iDevelopRepo.getRankTableData())
            fillAboutPreference(preference.getData())
        }
    }

}