package sgtmelon.scriptum.ui.dialog.preference

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.infrastructure.model.key.Repeat
import sgtmelon.scriptum.ui.dialog.parent.ParentSelectDialogUi

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