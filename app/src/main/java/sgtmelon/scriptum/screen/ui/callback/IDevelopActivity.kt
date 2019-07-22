package sgtmelon.scriptum.screen.ui.callback

import android.content.Intent
import sgtmelon.scriptum.screen.ui.DevelopActivity
import sgtmelon.scriptum.screen.vm.DevelopViewModel

/**
 * Interface for communication [DevelopViewModel] with [DevelopActivity]
 *
 * @author SerjantArbuz
 */
interface IDevelopActivity {

    fun startActivity(intent: Intent)

    fun fillAboutNoteTable(data: String)

    fun fillAboutRollTable(data: String)

    fun fillAboutRankTable(data: String)

    fun fillAboutPreference(data: String)

}