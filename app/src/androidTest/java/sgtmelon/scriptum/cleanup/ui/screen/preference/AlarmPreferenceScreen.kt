package sgtmelon.scriptum.cleanup.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.dialog.preference.VolumeDialogUi
import sgtmelon.scriptum.cleanup.ui.logic.preference.AlarmPreferenceLogic
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.parent.ui.parts.preferences.PreferencePart
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.MelodyDialogUi
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.RepeatDialogUi
import sgtmelon.scriptum.parent.ui.screen.dialogs.select.SignalDialogUi

/**
 * Class for UI control of [AlarmPreferenceFragment].
 */
class AlarmPreferenceScreen : PreferencePart<AlarmPreferenceLogic>(
    R.string.pref_title_alarm, TestViewTag.PREF_ALARM
) {

    override val screenLogic = AlarmPreferenceLogic()

    fun openRepeatDialog(func: RepeatDialogUi.() -> Unit = {}) {
        getItem(p = 1).Summary().onItemClick()
        RepeatDialogUi(func)
    }

    fun openSignalDialog(func: SignalDialogUi.() -> Unit = {}) {
        getItem(p = 2).Summary().onItemClick()
        SignalDialogUi(preferencesRepo.signalTypeCheck, func)
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
        inline operator fun invoke(func: AlarmPreferenceScreen.() -> Unit): AlarmPreferenceScreen {
            return AlarmPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}