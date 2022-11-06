package sgtmelon.scriptum.cleanup.ui.dialog.preference

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.dialog.parent.ParentSelectDialogUi
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.screen.preference.note.NotePreferenceFragment
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control of [SingleDialog] which open from [NotePreferenceFragment] for select sort.
 */
class SortDialogUi : ParentSelectDialogUi(
    R.string.pref_title_note_sort,
    R.array.pref_sort
) {

    override val initCheck: Int = preferencesRepo.sort.ordinal
    override var check: Int = initCheck

    fun onClickItem(sort: Sort) = apply {
        val position = sort.ordinal

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