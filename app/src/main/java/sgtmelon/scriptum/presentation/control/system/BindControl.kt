package sgtmelon.scriptum.presentation.control.system

import android.content.Context
import android.os.Build
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.getNotificationService
import sgtmelon.scriptum.extension.validIndexOfFirst
import sgtmelon.scriptum.extension.validRemoveAt
import sgtmelon.scriptum.presentation.control.system.callback.IBindControl
import sgtmelon.scriptum.presentation.factory.NotificationFactory as Factory

/**
 * Class for help control [NoteItem] notification bind in statusBar
 */
class BindControl(private val context: Context?) : IBindControl {

    private val manager = context?.getNotificationService()

    /**
     * Cached note list for binding in status bar.
     */
    private val noteItemList: MutableList<NoteItem> = ArrayList()

    /**
     * Use this list when you need save notes id for future unbind ([Tag.NOTE]).
     */
    private val noteIdList: MutableList<Int> = mutableListOf()

    /**
     * Because id's of [Tag.NOTE_GROUP] and [Tag.INFO] binds is const, you can
     * save it here for future unbind.
     */
    private val tagIdMap: MutableMap<String, Int> = mutableMapOf()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Factory.Count.createChannel(context)
            Factory.Notes.createChannel(context)
            Factory.deleteOldChannel(context)
        }
    }

    override fun notifyNotes(itemList: List<NoteItem>) {
        if (context == null) return

        clearRecent(Tag.NOTE)

        noteItemList.clearAdd(itemList)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (noteItemList.size > 1) {
                val summaryNotification = Factory.Notes.getBindSummary(context)

                manager?.notify(Tag.NOTE_GROUP, Id.NOTE_GROUP, summaryNotification)
                tagIdMap[Tag.NOTE_GROUP] = Id.NOTE_GROUP
            } else {
                clearRecent(Tag.NOTE_GROUP)
            }
        }

        for (item in noteItemList.reversed()) {
            val id = item.id.toInt()

            manager?.notify(Tag.NOTE, id, Factory.Notes[context, item])
            noteIdList.add(id)
        }
    }

    override fun cancelNote(id: Long) {
        val index = noteItemList.validIndexOfFirst { it.id == id } ?: return
        noteItemList.validRemoveAt(index) ?: return

        /**
         * Need copy [noteItemList] because inside [notifyNotes] happen call of [clearRecent]
         * and [noteItemList] will be cleared before show notes data in status bar.
         */
        notifyNotes(ArrayList(noteItemList))
    }

    override fun notifyCount(count: Int) {
        if (context == null) return

        if (count != 0) {
            manager?.notify(Tag.INFO, Id.INFO, Factory.Count[context, Id.INFO, count])
            tagIdMap[Tag.INFO] = Id.INFO
        } else {
            manager?.cancel(Tag.INFO, Id.INFO)
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
                    manager?.cancel(Tag.NOTE, id)
                }

                noteIdList.clear()
            }
            Tag.NOTE_GROUP, Tag.INFO -> {
                manager?.cancel(tag, tagIdMap.getOrElse(tag) { return })
                tagIdMap.remove(tag)
            }
            null -> {
                manager?.cancelAll()
                tagIdMap.clear()
            }
        }
    }

    @StringDef(Tag.NOTE, Tag.NOTE_GROUP, Tag.INFO)
    annotation class Tag {
        companion object {
            private const val PREFIX = "TAG_BIND"

            const val NOTE = "${PREFIX}_NOTE"
            const val NOTE_GROUP = "${PREFIX}_NOTE_GROUP"
            const val INFO = "${PREFIX}_INFO"
        }
    }

    /**
     * Id's for [Tag.NOTE_GROUP] and [Tag.INFO] notifications. For [Tag.NOTE] need
     * use [NoteItem.id].
     */
    @IntDef(Id.NOTE_GROUP, Id.INFO)
    private annotation class Id {
        companion object {
            const val NOTE_GROUP = 0
            const val INFO = 1
        }
    }

    companion object {
        @RunPrivate var instance: IBindControl? = null

        operator fun get(context: Context?): IBindControl {
            return instance ?: BindControl(context).also { instance = it }
        }
    }
}