package sgtmelon.scriptum.parent.ui.screen.dialogs.select

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.converter.key.RepeatConverter
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceFragment
import sgtmelon.scriptum.parent.ui.parts.dialog.SelectDialogPart
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [SingleDialog] which open from [NotePreferenceFragment] for select
 * alarm repeat.
 */
class RepeatDialogUi : SelectDialogPart<Repeat>(
    R.string.pref_title_alarm_repeat,
    R.array.pref_repeat
) {

    override val initCheck: Int = preferencesRepo.repeat.ordinal
    override var check: Int = initCheck

    override fun click(value: Repeat) {
        val position = RepeatConverter().toInt(value)

        check = position
        getItem(position).click()
        assert()
    }

    companion object {
        inline operator fun invoke(func: RepeatDialogUi.() -> Unit): RepeatDialogUi {
            return RepeatDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}