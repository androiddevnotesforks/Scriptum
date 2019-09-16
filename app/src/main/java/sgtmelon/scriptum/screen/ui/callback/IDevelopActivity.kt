package sgtmelon.scriptum.screen.ui.callback

import sgtmelon.scriptum.screen.ui.DevelopActivity
import sgtmelon.scriptum.screen.vm.DevelopViewModel

/**
 * Interface for communication [DevelopViewModel] with [DevelopActivity]
 */
interface IDevelopActivity {

    fun fillAboutNoteTable(data: String)

    fun fillAboutRollTable(data: String)

    fun fillAboutRankTable(data: String)

    fun fillAboutPreference(data: String)

    fun startIntroActivity()

}