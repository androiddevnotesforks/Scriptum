package sgtmelon.scriptum.presentation.screen.ui.callback

import sgtmelon.scriptum.presentation.screen.ui.impl.DevelopActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.IDevelopViewModel

/**
 * Interface for communication [IDevelopViewModel] with [DevelopActivity].
 */
interface IDevelopActivity {

    fun fillAboutNoteTable(data: String)

    fun fillAboutRollTable(data: String)

    fun fillAboutRankTable(data: String)

    fun fillAboutPreference(data: String)

    fun startIntroActivity()

}