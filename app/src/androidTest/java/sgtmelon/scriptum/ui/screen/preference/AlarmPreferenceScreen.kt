package sgtmelon.scriptum.ui.screen.preference

import sgtmelon.scriptum.R
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.ui.dialog.preference.RepeatDialogUi
import sgtmelon.scriptum.ui.logic.preference.AlarmPreferenceLogic

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