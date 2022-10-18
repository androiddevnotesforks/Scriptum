package sgtmelon.scriptum.cleanup.ui.dialog.preference

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.dialog.parent.ParentSelectDialogUi
import sgtmelon.scriptum.infrastructure.model.key.SavePeriod
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceFragment
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [SingleDialog] which open from [NotePreferenceFragment] for select
 * save period.
 */
class SavePeriodDialogUi : ParentSelectDialogUi(
    R.string.pref_title_note_save_period,
    R.array.pref_note_save_period
) {

    override val initCheck: Int = preferences.savePeriod
    override var check: Int = initCheck

    fun onClickItem(period: SavePeriod) = apply {
        val position = period.ordinal

        check = position
        getItem(position).click()
        assert()
    }

    companion object {
        operator fun invoke(func: SavePeriodDialogUi.() -> Unit): SavePeriodDialogUi {
            return SavePeriodDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}