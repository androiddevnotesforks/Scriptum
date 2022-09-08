package sgtmelon.scriptum.cleanup.ui.dialog.preference

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.cleanup.ui.dialog.parent.ParentSelectDialogUi
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [SingleDialog] which open from [NotePreferenceFragment] for select
 * alarm repeat.
 */
class RepeatDialogUi : ParentSelectDialogUi(
    R.string.pref_title_alarm_repeat,
    R.array.pref_alarm_repeat
) {

    override val initCheck: Int = preferences.repeat
    override var check: Int = initCheck

    fun onClickItem(repeat: Repeat) = apply {
        val position = repeat.ordinal

        check = position
        getItem(position).click()
        assert()
    }

    companion object {
        operator fun invoke(func: RepeatDialogUi.() -> Unit): RepeatDialogUi {
            return RepeatDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}