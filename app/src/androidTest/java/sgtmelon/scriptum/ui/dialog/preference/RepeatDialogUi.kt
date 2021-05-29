package sgtmelon.scriptum.ui.dialog.preference

import sgtmelon.safedialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.ui.dialog.parent.ParentSelectDialogUi

/**
 * Class for UI control of [SingleDialog] which open from [NotePreferenceFragment] for select
 * alarm repeat.
 */
class RepeatDialogUi : ParentSelectDialogUi(
    R.string.pref_title_alarm_repeat,
    R.array.pref_text_alarm_repeat
) {

    override val initCheck: Int = preferenceRepo.repeat
    override var check: Int = initCheck

    fun onClickItem(@Repeat value: Int) = apply {
        check = value

        getItem(textArray[value]).click()
        assert()
    }

    companion object {
        operator fun invoke(func: RepeatDialogUi.() -> Unit): RepeatDialogUi {
            return RepeatDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}