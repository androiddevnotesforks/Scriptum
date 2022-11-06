package sgtmelon.scriptum.cleanup.ui.dialog.preference

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.dialog.parent.ParentSelectDialogUi
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [SingleDialog] which open from [AlarmPreferenceFragment] for select
 * alarm melody.
 */
class MelodyDialogUi(
    textArray: Array<String>,
    override val initCheck: Int
) : ParentSelectDialogUi(
    R.string.pref_title_alarm_melody,
    textArray
) {

    override var check: Int = initCheck

    fun onClickItem(position: Int) = apply {
        check = position

        getItem(position).click()
        assert()
    }

    companion object {
        inline operator fun invoke(
            textArray: Array<String>,
            initCheck: Int,
            func: MelodyDialogUi.() -> Unit
        ): MelodyDialogUi {
            return MelodyDialogUi(textArray, initCheck).apply { waitOpen { assert() } }.apply(func)
        }
    }
}