package sgtmelon.scriptum.parent.ui.screen.dialogs.select

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceFragment
import sgtmelon.scriptum.parent.ui.parts.dialog.SelectDialogPart
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [SingleDialog] which open from [NotePreferenceFragment] for select sort.
 */
class SortDialogUi : SelectDialogPart<Sort>(
    R.string.pref_title_note_sort,
    R.array.pref_sort
) {

    override val initCheck: Int = preferencesRepo.sort.ordinal
    override var check: Int = initCheck

    override fun click(value: Sort) {
        val position = SortConverter().toInt(value)

        check = position
        getItem(position).click()
        assert()
    }

    companion object {
        inline operator fun invoke(func: SortDialogUi.() -> Unit): SortDialogUi {
            return SortDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}