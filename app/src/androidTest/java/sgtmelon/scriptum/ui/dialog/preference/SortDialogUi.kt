package sgtmelon.scriptum.ui.dialog.preference

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.click
import sgtmelon.scriptum.cleanup.domain.model.annotation.Sort
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.NotePreferenceFragment
import sgtmelon.scriptum.ui.dialog.parent.ParentSelectDialogUi

/**
 * Class for UI control of [SingleDialog] which open from [NotePreferenceFragment] for select sort.
 */
class SortDialogUi : ParentSelectDialogUi(
    R.string.pref_title_note_sort,
    R.array.pref_note_sort
) {

    override val initCheck: Int = preferences.sort
    override var check: Int = initCheck

    fun onClickItem(@Sort position: Int) = apply {
        check = position

        getItem(position).click()
        assert()
    }

    companion object {
        operator fun invoke(func: SortDialogUi.() -> Unit): SortDialogUi {
            return SortDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}