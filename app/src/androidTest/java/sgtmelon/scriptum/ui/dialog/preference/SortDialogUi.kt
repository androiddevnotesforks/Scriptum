package sgtmelon.scriptum.ui.dialog.preference

import sgtmelon.safedialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.ui.dialog.parent.ParentSelectDialogUi

/**
 * Class for UI control of [SingleDialog] which open from [NotePreferenceFragment] for select sort.
 */
class SortDialogUi : ParentSelectDialogUi(
    R.string.pref_title_note_sort,
    R.array.pref_text_note_sort
) {

    override val initCheck: Int = preferenceRepo.sort
    override var check: Int = initCheck

    fun onClickItem(@Sort value: Int) = apply {
        check = value

        getItem(textArray[value]).click()
        assert()
    }

    companion object {
        operator fun invoke(func: SortDialogUi.() -> Unit): SortDialogUi {
            return SortDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}