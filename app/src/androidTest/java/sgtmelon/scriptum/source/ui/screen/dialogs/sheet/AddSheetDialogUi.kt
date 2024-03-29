package sgtmelon.scriptum.source.ui.screen.dialogs.sheet

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.dialogs.sheet.AddSheetDialog
import sgtmelon.scriptum.cleanup.ui.screen.note.RollNoteScreen
import sgtmelon.scriptum.cleanup.ui.screen.note.TextNoteScreen
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.source.ui.model.key.NoteState
import sgtmelon.scriptum.source.ui.parts.dialog.SheetDialogPart
import sgtmelon.scriptum.source.ui.screen.note.NoteScreen
import sgtmelon.test.cappuccino.utils.awaitMinuteEnd
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

    @Deprecated("Use create text")
    fun createText(
        item: NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) {
        awaitMinuteEnd()
        getButton(NoteType.TEXT).click()
        NoteScreen().openText(func, NoteState.NEW, item, isRankEmpty)
    }

    @Deprecated("Use create roll")
    fun createRoll(
        item: NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        awaitMinuteEnd()
        getButton(NoteType.ROLL).click()
        NoteScreen().openRoll(func, NoteState.NEW, item, isRankEmpty)
    }

    fun createText(
        create: () -> NoteItem.Text,
        isRankEmpty: Boolean = true,
        func: TextNoteScreen.() -> Unit = {}
    ) {
        val item = create()
        getButton(NoteType.TEXT).click()
        NoteScreen().openText(func, NoteState.NEW, item, isRankEmpty)
    }

    fun createRoll(
        create: () -> NoteItem.Roll,
        isRankEmpty: Boolean = true,
        func: RollNoteScreen.() -> Unit = {}
    ) {
        val item = create()
        getButton(NoteType.ROLL).click()
        NoteScreen().openRoll(func, NoteState.NEW, item, isRankEmpty)
    }

    companion object {
        inline operator fun invoke(func: AddSheetDialogUi.() -> Unit): AddSheetDialogUi {
            return AddSheetDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}