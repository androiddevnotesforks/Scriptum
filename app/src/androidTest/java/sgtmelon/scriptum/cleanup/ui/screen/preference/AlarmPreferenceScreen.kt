package sgtmelon.scriptum.cleanup.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.dialog.preference.MelodyDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.preference.RepeatDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.preference.SignalDialogUi
import sgtmelon.scriptum.cleanup.ui.dialog.preference.VolumeDialogUi
import sgtmelon.scriptum.cleanup.ui.logic.preference.AlarmPreferenceLogic
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment

/**
 * Class for UI control of [AlarmPreferenceFragment].
 */
class AlarmPreferenceScreen :
    ParentPreferenceScreen<AlarmPreferenceLogic>(R.string.pref_title_alarm) {

    override val screenLogic = AlarmPreferenceLogic()

    fun openRepeatDialog(func: RepeatDialogUi.() -> Unit = {}) {
        getItem(p = 1).Summary().onItemClick()
        RepeatDialogUi(func)
    }

    fun openSignalDialog(func: SignalDialogUi.() -> Unit = {}) {
        getItem(p = 2).Summary().onItemClick()
        SignalDialogUi(screenLogic.preferencesRepo.signalTypeCheck, func)
    }

    fun openMelodyDialog(func: MelodyDialogUi.() -> Unit = {}) {
        getItem(p = 4).Summary().onItemClick()

        val pair = screenLogic.getMelodyDialogPair()
        MelodyDialogUi(pair.first, pair.second, func)
    }

    fun openVolumeDialog(func: VolumeDialogUi.() -> Unit = {}) {
        getItem(p = 5).Summary().onItemClick()
        VolumeDialogUi(func)
    }

    fun onVolumeIncreaseClick() {
        getItem(p = 6).Switch().onItemClick()
        assert()
    }

    companion object {
        operator fun invoke(func: AlarmPreferenceScreen.() -> Unit): AlarmPreferenceScreen {
            return AlarmPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}