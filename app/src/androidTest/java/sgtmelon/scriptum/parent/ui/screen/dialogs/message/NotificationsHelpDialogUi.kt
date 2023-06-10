package sgtmelon.scriptum.parent.ui.screen.dialogs.message

import androidx.annotation.AttrRes
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ui.parts.dialog.MessageDialogPart


/**
 * Class for UI control of [MessageDialog] when user notifications help dialog is showed
 * on start.
 */
class NotificationsHelpDialogUi : MessageDialogPart(
    R.string.dialog_title_notifications,
    R.string.dialog_text_notifications,
    R.string.dialog_notifications_settings,
    R.string.dialog_notifications_channel,
    R.string.dialog_notifications_done
) {
    @get:AttrRes override val negativeAttr: Int = R.attr.clAccent
    @get:AttrRes override val neutralAttr: Int = R.attr.clContentSecond

    companion object {
        inline operator fun invoke(
            func: NotificationsHelpDialogUi.() -> Unit
        ): NotificationsHelpDialogUi {
            return NotificationsHelpDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}