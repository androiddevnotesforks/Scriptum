package sgtmelon.scriptum.infrastructure.model.annotation.notifications

import androidx.annotation.IntDef
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem

/**
 * For manage all notifications id's.
 */
@IntDef(NotificationId.SERVICE, NotificationId.NOTE_GROUP, NotificationId.NOTIFICATIONS_COUNT)
annotation class NotificationId {

    /** For [NotificationTag.NOTE] will be used [NoteItem.id]. */
    companion object {
        const val SERVICE = -1
        const val NOTE_GROUP = -2
        const val NOTIFICATIONS_COUNT = -3
    }
}