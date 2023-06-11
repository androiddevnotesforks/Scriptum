package sgtmelon.scriptum.source.ui.screen.dialogs.select

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotesPreferenceFragment
import sgtmelon.scriptum.source.ui.parts.dialog.SelectDialogPart
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [SingleDialog] which open from [NotesPreferenceFragment] for select
 * save period.
 */
class SavePeriodDialogUi : SelectDialogPart<SavePeriod>(
    R.string.pref_title_note_save_period,
    R.array.pref_save_period
) {

    override val initCheck: Int = preferencesRepo.savePeriod.ordinal
    override var check: Int = initCheck

    override fun click(value: SavePeriod) {
        val position = SavePeriodConverter().toInt(value)

        check = position
        getItem(position).click()
        assert()
    }

    companion object {
        inline operator fun invoke(func: SavePeriodDialogUi.() -> Unit): SavePeriodDialogUi {
            return SavePeriodDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}