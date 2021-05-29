package sgtmelon.scriptum.ui.dialog.preference

import sgtmelon.safedialog.MultipleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.AlarmPreferenceFragment
import sgtmelon.scriptum.ui.dialog.parent.ParentMultipleDialogUi

/**
 * Class for UI control of [MultipleDialog] which open from [AlarmPreferenceFragment] for select
 * signal type.
 */
class SignalDialogUi(
    override val initCheck: BooleanArray
) : ParentMultipleDialogUi(
    R.string.pref_title_alarm_signal,
    R.array.pref_text_alarm_signal,
    needOneSelect = true
) {

    override val check: BooleanArray = initCheck.clone()

    fun onClickItem(position: Int) = apply {
        val value = check[position]
        check[position] = !value

        getItem(position).click()
        assert()
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