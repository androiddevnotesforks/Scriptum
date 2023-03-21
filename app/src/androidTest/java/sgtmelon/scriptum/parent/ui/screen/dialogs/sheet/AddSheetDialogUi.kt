package sgtmelon.scriptum.parent.ui.screen.dialogs.sheet

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.dialog.sheet.AddSheetDialog
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.parts.dialog.SheetDialogPart
import sgtmelon.scriptum.parent.ui.screen.note.NoteScreen
import sgtmelon.test.cappuccino.utils.click

/**
 * Class for UI control [AddSheetDialog].
 */
class AddSheetDialogUi : SheetDialogPart(
    R.id.add_container,
    R.id.add_navigation,
    R.string.dialog_title_add_note,
    R.array.dialog_add
) {

    fun createText(
        item: NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) {
        getButton(NoteType.TEXT).click()
        NoteScreen().openText(func, NoteState.NEW, item, isRankEmpty)
    }

    fun createRoll(
        item: NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        getButton(NoteType.ROLL).click()
        NoteScreen().openRoll(func, NoteState.NEW, item, isRankEmpty)
    }

    companion object {
        inline operator fun invoke(func: AddSheetDialogUi.() -> Unit): AddSheetDialogUi {
            return AddSheetDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}