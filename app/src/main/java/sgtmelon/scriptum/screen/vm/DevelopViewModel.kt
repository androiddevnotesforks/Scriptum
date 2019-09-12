package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.repository.room.develop.DevelopRepo
import sgtmelon.scriptum.repository.room.develop.IDevelopRepo
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

    private val iDevelopRepo: IDevelopRepo = DevelopRepo(context)

    override fun onSetup(bundle: Bundle?) {
        viewModelScope.launch {
            callback?.apply {
                fillAboutNoteTable(iDevelopRepo.getNoteTablePrint())
                fillAboutRollTable(iDevelopRepo.getRollTablePrint())
                fillAboutRankTable(iDevelopRepo.getRankTablePrint())
                fillAboutPreference(iDevelopRepo.getPreferencePrint())
            }
        }
    }

    override fun onIntroClick() {
        callback?.startActivity(IntroActivity.getInstance(context))
    }

}