package sgtmelon.scriptum.source.ui.screen.dialogs.message

import sgtmelon.safedialog.annotation.MessageType
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.note.NoteActivity
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type
import sgtmelon.scriptum.source.ui.parts.dialog.MessageDialogPart

/**
 * Class for UI control of [MessageDialog] which open from [NoteActivity] on convert.
 */
class ConvertDialogUi(
    item: NoteItem,
    private val callback: Callback
) : MessageDialogPart(
    R.string.dialog_title_convert,
    when (item.type) {
        NoteType.TEXT -> R.string.dialog_text_convert_text
        NoteType.ROLL -> R.string.dialog_roll_convert_roll
    },
    MessageType.Choice
) {

    override fun onPositiveResult() = callback.onConvertDialogResult()

    interface Callback {
        fun onConvertDialogResult()
    }

    companion object {
        inline operator fun invoke(
            func: ConvertDialogUi.() -> Unit,
            item: NoteItem,
            callback: Callback
        ): ConvertDialogUi {
            return ConvertDialogUi(item, callback).apply { waitOpen { assert() } }.apply(func)
        }
    }
}