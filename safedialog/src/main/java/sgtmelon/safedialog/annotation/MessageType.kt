package sgtmelon.safedialog.annotation

import androidx.annotation.IntDef

@IntDef(MessageType.INFO, MessageType.CHOICE)
annotation class MessageType {

    companion object {
        const val INFO = 0
        const val CHOICE = 1
    }
}