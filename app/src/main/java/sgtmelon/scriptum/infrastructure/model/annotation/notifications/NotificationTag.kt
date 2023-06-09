package sgtmelon.scriptum.infrastructure.model.annotation.notifications

import androidx.annotation.StringDef

/**
 * For manage notifications tags.
 */
@StringDef(NotificationTag.NOTE, NotificationTag.NOTE_GROUP, NotificationTag.INFO)
annotation class NotificationTag {
    companion object {
        const val NOTE = "TAG_BIND_NOTE"
        const val NOTE_GROUP = "TAG_BIND_NOTE_GROUP"
        const val INFO = "TAG_BIND_INFO"
    }
}
