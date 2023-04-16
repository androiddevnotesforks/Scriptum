package sgtmelon.scriptum.parent.ui.screen.dialogs.sheet

import java.util.Calendar
import sgtmelon.extensions.getCalendar
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.dialog.sheet.AddSheetDialog
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.parent.ui.model.key.NoteState
import sgtmelon.scriptum.parent.ui.parts.dialog.SheetDialogPart
import sgtmelon.scriptum.parent.ui.screen.note.NoteScreen
import sgtmelon.test.cappuccino.utils.await
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

    /** Try to escape cases when model and note have different creation time. */
    private fun awaitMinuteEnd() {
        while (getCalendar().get(Calendar.SECOND) > 50) {
            await(time = 1000)
        }
    }

    fun createText(
        item: NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) {
        awaitMinuteEnd()
        getButton(NoteType.TEXT).click()
        NoteScreen().openText(func, NoteState.NEW, item, isRankEmpty)
    }

    fun createRoll(
        item: NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        awaitMinuteEnd()
        getButton(NoteType.ROLL).click()
        NoteScreen().openRoll(func, NoteState.NEW, item, isRankEmpty)
    }

    companion object {
        inline operator fun invoke(func: AddSheetDialogUi.() -> Unit): AddSheetDialogUi {
            return AddSheetDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}