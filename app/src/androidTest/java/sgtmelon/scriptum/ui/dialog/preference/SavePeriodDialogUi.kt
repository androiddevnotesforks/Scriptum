package sgtmelon.scriptum.ui.dialog.preference

import sgtmelon.safedialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment

/**
 * Class for UI control of [SingleDialog] which open from [NotePreferenceFragment] for select
 * save period.
 */
class SavePeriodDialogUi : ParentSelectDialogUi(
    R.string.pref_title_note_save_period,
    R.array.pref_text_note_save_period
) {

    override val initCheck: Int = preferenceRepo.savePeriod
    override var check: Int = preferenceRepo.savePeriod

    fun onClickItem(@SavePeriod value: Int) = apply {
        check = value

        getItem(textArray[value]).click()
        assert()
    }

    companion object {
        operator fun invoke(func: SavePeriodDialogUi.() -> Unit): SavePeriodDialogUi {
            return SavePeriodDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}