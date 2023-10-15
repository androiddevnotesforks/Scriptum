package sgtmelon.scriptum.source.ui.screen.preference.alarm

import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.ui.parts.preferences.PreferencePart
import sgtmelon.scriptum.source.ui.screen.dialogs.permissions.deny.MelodyAccessDialogUi
import sgtmelon.scriptum.source.ui.screen.dialogs.preference.VolumeDialogUi
import sgtmelon.scriptum.source.ui.screen.dialogs.select.MelodyDialogUi
import sgtmelon.scriptum.source.ui.screen.dialogs.select.RepeatDialogUi
import sgtmelon.scriptum.source.ui.screen.dialogs.select.SignalDialogUi
import sgtmelon.scriptum.source.ui.screen.preference.alarm.AlarmPreferenceLogic.Part

/**
 * Class for UI control of [AlarmPreferenceFragment].
 */
class AlarmPreferenceScreen : PreferencePart<AlarmPreferenceLogic>(
    R.string.pref_title_alarm, TestViewTag.PREF_ALARM
) {

    override val screenLogic = AlarmPreferenceLogic()

    fun openSignalDialog(func: SignalDialogUi.() -> Unit = {}) {
        getItem(Part.SIGNAL_ITEM).Summary().onItemClick()
        SignalDialogUi(preferencesRepo.signalTypeCheck, func)
    }

    fun openRepeatDialog(func: RepeatDialogUi.() -> Unit = {}) {
        getItem(Part.REPEAT_ITEM).Summary().onItemClick()
        RepeatDialogUi(func)
    }

    fun melodyPermission(func: MelodyAccessDialogUi.() -> Unit = {}) {
        getItem(Part.MELODY_ITEM).Summary().onItemClick()

        val (textArray, initCheck) = screenLogic.getMelodyDialogPair()
        MelodyAccessDialogUi(textArray, initCheck, func)
    }

    fun openMelodyDialog(func: MelodyDialogUi.() -> Unit = {}) {
        getItem(Part.MELODY_ITEM).Summary().onItemClick()

        val (textArray, initCheck) = screenLogic.getMelodyDialogPair()
        MelodyDialogUi(textArray, initCheck, func)
    }

    fun openVolumeDialog(func: VolumeDialogUi.() -> Unit = {}) {
        getItem(Part.VOLUME_ITEM).Summary().onItemClick()
        VolumeDialogUi(func)
    }

    fun onVolumeIncreaseClick() {
        getItem(Part.INCREASE_ITEM).Switch().onItemClick()
        assert()
    }

    companion object {
        inline operator fun invoke(func: AlarmPreferenceScreen.() -> Unit): AlarmPreferenceScreen {
            return AlarmPreferenceScreen().apply { assert() }.apply(func)
        }
    }
}