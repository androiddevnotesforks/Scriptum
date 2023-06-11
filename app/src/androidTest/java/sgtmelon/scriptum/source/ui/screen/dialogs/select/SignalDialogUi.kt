package sgtmelon.scriptum.source.ui.screen.dialogs.select

import sgtmelon.safedialog.dialog.MultipleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.scriptum.source.ui.parts.dialog.MultipleDialogUi
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [MultipleDialog] which open from [AlarmPreferenceFragment] for select
 * signal type.
 */
class SignalDialogUi(
    override val initCheck: BooleanArray
) : MultipleDialogUi(
    R.string.pref_title_alarm_signal,
    R.array.pref_signal,
    needOneSelect = true
) {

    override val check: BooleanArray = initCheck.clone()

    fun click(position: Int) = apply {
        val value = check[position]
        check[position] = !value

        getItem(position).click()
        assert()
    }

    fun click(newCheck: BooleanArray) = apply {
        for ((i, item) in newCheck.withIndex()) {
            if (item != check[i]) click(i)
        }
    }

    companion object {
        inline operator fun invoke(
            initCheck: BooleanArray,
            func: SignalDialogUi.() -> Unit
        ): SignalDialogUi {
            return SignalDialogUi(initCheck).apply { waitOpen { assert() } }.apply(func)
        }
    }
}