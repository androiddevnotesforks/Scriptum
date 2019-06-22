package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.content.Intent
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.repository.develop.DevelopRepo
import sgtmelon.scriptum.screen.callback.IDevelopActivity
import sgtmelon.scriptum.screen.view.DevelopActivity
import sgtmelon.scriptum.screen.view.intro.IntroActivity

/**
 * ViewModel для [DevelopActivity]
 *
 * @author SerjantArbuz
 */
class DevelopViewModel(application: Application) : ParentViewModel(application) {

    lateinit var callback: IDevelopActivity

    private val iDevelopRepo = DevelopRepo.getInstance(context)

    fun onSetup() = viewModelScope.launch {
        callback.apply {
            fillAboutNoteTable(iDevelopRepo.getNoteTableData())
            fillAboutRollTable(iDevelopRepo.getRollTableData())
            fillAboutRankTable(iDevelopRepo.getRankTableData())
            fillAboutPreference(iPreferenceRepo.getData())
        }
    }

    fun onIntroClick() = callback.startActivity(Intent(context, IntroActivity::class.java))

}