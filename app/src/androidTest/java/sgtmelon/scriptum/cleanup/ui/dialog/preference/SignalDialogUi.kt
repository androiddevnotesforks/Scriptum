package sgtmelon.scriptum.cleanup.ui.dialog.preference

import sgtmelon.safedialog.dialog.MultipleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.dialog.parent.ParentMultipleDialogUi
import sgtmelon.scriptum.infrastructure.screen.preference.alarm.AlarmPreferenceFragment
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [MultipleDialog] which open from [AlarmPreferenceFragment] for select
 * signal type.
 */
class SignalDialogUi(
    override val initCheck: BooleanArray
) : ParentMultipleDialogUi(
    R.string.pref_title_alarm_signal,
    R.array.pref_signal,
    needOneSelect = true
) {

    override val check: BooleanArray = initCheck.clone()

    fun onClickItem(position: Int) = apply {
        val value = check[position]
        check[position] = !value

        getItem(position).click()
        assert()
    }

    fun onClickItem(newCheck: BooleanArray) = apply {
        for ((i, item) in newCheck.withIndex()) {
            if (item == check[i]) continue

            onClickItem(i)
        }
    }

    companion object {
        operator fun invoke(
            initCheck: BooleanArray,
            func: SignalDialogUi.() -> Unit
        ): SignalDialogUi {
            return SignalDialogUi(initCheck).apply { waitOpen { assert() } }.apply(func)
        }
    }
}