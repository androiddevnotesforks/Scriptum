package sgtmelon.scriptum.screen.vm

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.repository.develop.DevelopRepo
import sgtmelon.scriptum.screen.ui.DevelopActivity
import sgtmelon.scriptum.screen.ui.callback.IDevelopActivity
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.vm.callback.IDevelopViewModel

/**
 * ViewModel for [DevelopActivity]
 *
 * @author SerjantArbuz
 */
class DevelopViewModel(application: Application) : ParentViewModel<IDevelopActivity>(application),
        IDevelopViewModel {

    private val iDevelopRepo = DevelopRepo.getInstance(context)

    override fun onSetup() = viewModelScope.launch {
        callback?.apply {
            fillAboutNoteTable(iDevelopRepo.getNoteTablePrint())
            fillAboutRollTable(iDevelopRepo.getRollTablePrint())
            fillAboutRankTable(iDevelopRepo.getRankTablePrint())
            fillAboutPreference(iDevelopRepo.getPreferencePrint())
        }
    }

    override fun onIntroClick() {
        callback?.startActivity(IntroActivity.getInstance(context))
    }

}