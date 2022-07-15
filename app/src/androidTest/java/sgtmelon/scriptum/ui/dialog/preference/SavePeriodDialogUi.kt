package sgtmelon.scriptum.ui.dialog.preference

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.cleanup.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.ui.dialog.parent.ParentSelectDialogUi

/**
 * Class for UI control of [SingleDialog] which open from [NotePreferenceFragment] for select
 * save period.
 */
class SavePeriodDialogUi : ParentSelectDialogUi(
    R.string.pref_title_note_save_period,
    R.array.pref_text_note_save_period
) {

    override val initCheck: Int = appPreferences.savePeriod
    override var check: Int = initCheck

    fun onClickItem(@SavePeriod position: Int) = apply {
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