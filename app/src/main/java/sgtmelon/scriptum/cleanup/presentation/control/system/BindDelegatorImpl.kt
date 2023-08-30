package sgtmelon.scriptum.cleanup.presentation.control.system

import android.app.NotificationManager
import android.content.Context
import sgtmelon.extensions.getNotificationService
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.extension.validIndexOfFirst
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd
import sgtmelon.scriptum.infrastructure.utils.extensions.removeAtOrNull
import sgtmelon.scriptum.cleanup.presentation.factory.NotificationFactory as Factory
import sgtmelon.scriptum.infrastructure.model.annotation.notifications.NotificationId as Id
import sgtmelon.scriptum.infrastructure.model.annotation.notifications.NotificationTag as Tag

/**
 * Class for help control [NoteItem] notification bind in statusBar
 */
class BindDelegatorImpl(private val context: Context) : BindDelegator {

    private val manager: NotificationManager = context.getNotificationService()

    /** Cached note list for binding in status bar. */
    private val noteItemList: MutableList<NoteItem> = ArrayList()

    /** Use this list when you need save notes id for future unbind ([Tag.NOTE]). */
    private val noteIdList: MutableList<Int> = mutableListOf()

    /**
     * Because id's of [Tag.NOTE_GROUP] and [Tag.INFO] binds is const, you can
     * save it here for future unbind.
     */
    private val tagIdMap: MutableMap<String, Int> = mutableMapOf()

    init {
        Factory.Count.createChannel(context)
        Factory.Notes.createChannel(context)
        Factory.deleteOldChannels(context)
    }

    override fun notifyNotes(list: List<NoteItem>) {
        clearRecent(Tag.NOTE)

        noteItemList.clearAdd(list)

        if (noteItemList.size > 1) {
            val summaryNotification = Factory.Notes.getBindSummary(context)
            manager.notify(Tag.NOTE_GROUP, Id.NOTE_GROUP, summaryNotification)
            tagIdMap[Tag.NOTE_GROUP] = Id.NOTE_GROUP
        } else {
            clearRecent(Tag.NOTE_GROUP)
        }

        for (item in noteItemList.reversed()) {
            val id = item.id.toInt()

            manager.notify(Tag.NOTE, id, Factory.Notes[context, item])
            noteIdList.add(id)
        }
    }

    override fun cancelNote(id: Long) {
        val index = noteItemList.validIndexOfFirst { it.id == id } ?: return
        noteItemList.removeAtOrNull(index) ?: return

        /**
         * Need copy [noteItemList] because inside [notifyNotes] happen call of [clearRecent]
         * and [noteItemList] will be cleared before show notes data in status bar.
         */
        notifyNotes(ArrayList(noteItemList))
    }

    override fun notifyCount(count: Int) {
        val id = Id.NOTIFICATIONS_COUNT

        if (count != 0) {
            manager.notify(Tag.INFO, id, Factory.Count[context, id, count])
            tagIdMap[Tag.INFO] = id
        } else {
            manager.cancel(Tag.INFO, id)
        }
    }

    /**
     * If [tag] equals [Tag.NOTE], you must use [noteIdList]. Because it's impossible save
     * different notes id in [tagIdMap].
     *
     * If [tag] equals null, then need cancel all binds from [tagIdMap].
     */
    override fun clearRecent(@Tag tag: String?) {
        when (tag) {
            Tag.NOTE -> {
                for (id in noteIdList) {
                    manager.cancel(Tag.NOTE, id)
                }

                noteIdList.clear()
            }
            Tag.NOTE_GROUP, Tag.INFO -> {
                manager.cancel(tag, tagIdMap.getOrElse(tag) { return })
                tagIdMap.remove(tag)
            }
            null -> {
                manager.cancelAll()
                tagIdMap.clear()
            }
        }
    }

    companion object {
        /** Singleton needed for tests. */
        private var instance: BindDelegator? = null

        operator fun get(context: Context): BindDelegator {
            return instance ?: BindDelegatorImpl(context).also { instance = it }
        }
    }
}