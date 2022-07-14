package sgtmelon.safedialog.annotation

import sgtmelon.safedialog.dialog.MessageDialog

/**
 * Enum class for choose between different types of [MessageDialog].
 *
 * [INFO] - With single button (OK)
 * [CHOICE] - With two buttons (NO/YES)
 */
enum class MessageType { INFO, CHOICE }