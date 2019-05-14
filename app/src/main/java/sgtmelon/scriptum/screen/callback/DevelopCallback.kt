package sgtmelon.scriptum.screen.callback

import sgtmelon.scriptum.screen.view.DevelopActivity
import sgtmelon.scriptum.screen.vm.DevelopViewModel

/**
 * Интерфейс для обзения [DevelopViewModel] с [DevelopActivity]
 *
 * @author SerjantArbuz
 */
interface DevelopCallback {

    fun fillAboutNoteTable(data: String)

    fun fillAboutRollTable(data: String)

    fun fillAboutRankTable(data: String)

    fun fillAboutPreference(data: String)

}