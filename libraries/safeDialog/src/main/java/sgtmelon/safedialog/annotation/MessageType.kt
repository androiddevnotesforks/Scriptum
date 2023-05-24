package sgtmelon.safedialog.annotation

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import sgtmelon.safedialog.R
import sgtmelon.safedialog.dialog.MessageDialog

/**
 * Enum class for choose between different types of [MessageDialog].
 *
 * [Info] - With single button (OK)
 * [Choice] - With two buttons (NO/YES)
 */
@Serializable
sealed class MessageType {

    @get:StringRes abstract val positiveButton: Int
    @get:StringRes abstract val negativeButton: Int?
    @get:StringRes abstract val neutralButton: Int?

    object Info : MessageType() {
        override val positiveButton = R.string.dialog_button_ok
        override val negativeButton = null
        override val neutralButton = null
    }

    object Choice : MessageType() {
        override val positiveButton = R.string.dialog_button_yes
        override val negativeButton = R.string.dialog_button_no
        override val neutralButton = null
    }

    @Serializable
    data class Custom(
        override val positiveButton: Int,
        override val negativeButton: Int?,
        override val neutralButton: Int?
    ) : MessageType()

}