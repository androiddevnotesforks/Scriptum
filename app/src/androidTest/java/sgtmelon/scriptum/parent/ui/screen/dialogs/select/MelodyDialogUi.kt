package sgtmelon.scriptum.parent.ui.screen.dialogs.select

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.parent.ui.parts.dialog.SelectDialogPart
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [SingleDialog] which open from [AlarmPreferenceFragment] for select
 * alarm melody.
 */
class MelodyDialogUi(
    textArray: Array<String>,
    override val initCheck: Int
) : SelectDialogPart<Int>(
    R.string.pref_title_alarm_melody,
    textArray
) {

    override var check: Int = initCheck

    override fun click(value: Int) {
        check = value

        getItem(value).click()
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