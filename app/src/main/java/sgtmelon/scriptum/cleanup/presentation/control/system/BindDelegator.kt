package sgtmelon.scriptum.cleanup.presentation.control.system

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.infrastructure.model.annotation.notifications.NotificationTag as Tag

/**
 * Interface for communicate with [BindDelegatorImpl].
 */
interface BindDelegator {

    fun notifyNotes(list: List<NoteItem>)

    fun cancelNote(id: Long)

    fun notifyCount(count: Int)

    /** Clear notifications on test tearDown. */
    fun clearRecent(@Tag tag: String? = null)

}