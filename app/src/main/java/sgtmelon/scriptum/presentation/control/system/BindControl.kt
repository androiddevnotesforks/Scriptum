package sgtmelon.scriptum.presentation.control.system

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import androidx.annotation.StringDef
import androidx.annotation.VisibleForTesting
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.extension.clearAddAll
import sgtmelon.scriptum.extension.sort
import sgtmelon.scriptum.presentation.control.system.callback.IBindControl
import sgtmelon.scriptum.presentation.factory.NotificationFactory

/**
 * Class for help control [NoteItem] notification bind in statusBar
 */
class BindControl(private val context: Context?) : IBindControl {

    private val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE)
            as? NotificationManager

    private val noteItemList: MutableList<NoteItem> = ArrayList()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && context != null) {
            manager?.createNotificationChannel(getInfoChannel(context))
            manager?.createNotificationChannel(getNotesChannel(context))

            deleteOldChannel(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInfoChannel(context: Context) : NotificationChannel {
        val id = context.getString(R.string.notification_info_channel_id)
        val name = context.getString(R.string.notification_info_channel)

        return NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
            setSound(null, null)
            vibrationPattern = null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotesChannel(context: Context) : NotificationChannel {
        val id = context.getString(R.string.notification_notes_channel_id)
        val name = context.getString(R.string.notification_notes_channel)

        return NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
            setSound(null, null)
            vibrationPattern = null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteOldChannel(context: Context) {
        val id = context.getString(R.string.notification_old_channel_id)
        manager?.deleteNotificationChannel(id)
    }


    /**
     * Update notification if note isStatus and isVisible, otherwise cancel notification
     */
    override fun notifyNote(@Sort sort: Int, noteItem: NoteItem, rankIdVisibleList: List<Long>) {
        if (context == null) return

        val index = noteItemList.indexOfFirst { it.id == noteItem.id }
        if (index != -1) {
            noteItemList[index] = noteItem
        } else {
            noteItemList.add(noteItem)
        }

        notifyNote(sort, noteItemList, rankIdVisibleList)
    }

    override fun notifyNote(@Sort sort: Int, itemList: List<NoteItem>,
                            rankIdVisibleList: List<Long>) {
        if (context == null) return

        clearRecent(Tag.NOTE)

        noteItemList.clearAddAll(itemList.filter {
            !it.isBin && it.isStatus && it.isVisible(rankIdVisibleList)
        }.sort(sort))

        noteItemList.reversed().forEach {
            val id = it.id.toInt()

             manager?.notify(Tag.NOTE, id, NotificationFactory.getBind(context, it))
            tagIdMap[Tag.NOTE] = id
        }

        // TODO pass summary or list
        manager?.notify(Tag.OTHER, Id.NOTE_GROUP, NotificationFactory.getBingSummary(context))
        tagIdMap[Tag.OTHER] = Id.NOTE_GROUP
    }

    override fun cancelNote(id: Int) {
        manager?.cancel(Tag.NOTE, id)
    }

    override fun notifyInfo(count: Int) {
        if (context == null) return

        if (count != 0) {
            manager?.notify(Tag.OTHER, Id.INFO, NotificationFactory.getInfo(context, count))
            tagIdMap[Tag.OTHER] = Id.INFO
        } else {
            manager?.cancel(Tag.OTHER, Id.INFO)
        }
    }

    override fun clearRecent(@Tag tag: String?) {
        if (tag == null) {
            tagIdMap.forEach { manager?.cancel(it.key, it.value) }
            tagIdMap.clear()
        } else {
            tagIdMap.filterKeys { it == tag }.forEach { manager?.cancel(it.key, it.value) }
            tagIdMap.remove(tag)
        }
    }


    /**
     * Callback which need implement in interface what pass to Interactor
     * Use when needs get access to [notifyNote] and [cancelNote] inside Interactor
     */
    interface NoteBridge {
        interface Full : Notify, Cancel

        interface Notify {
            fun notifyNoteBind(@Sort sort: Int, item: NoteItem, rankIdVisibleList: List<Long>)
        }

        interface NotifyAll {
            fun notifyNoteBind(@Sort sort: Int, itemList: List<NoteItem>,
                               rankIdVisibleList: List<Long>)
        }

        interface Cancel {
            fun cancelNoteBind(id: Int)
        }
    }

    /**
     * Use when needs get access to [notifyInfo] inside Interactor
     */
    interface InfoBridge {
        fun notifyInfoBind(count: Int)
    }

    @StringDef(Tag.NOTE, Tag.OTHER)
    annotation class Tag {
        companion object {
            private const val PREFIX = "TAG_BIND"

            const val NOTE = "${PREFIX}_NOTE"
            const val OTHER = "${PREFIX}_OTHER"
        }
    }

    /**
     * Id's for [Tag.OTHER] notifications.
     */
    @IntDef(Id.NOTE_GROUP, Id.INFO)
    annotation class Id {
        companion object {
            const val NOTE_GROUP = 0
            const val INFO = 1
        }
    }

    companion object {
        const val INFO_ID = 0

        @VisibleForTesting
        var callback: IBindControl? = null

        @VisibleForTesting
        val tagIdMap: MutableMap<String, Int> = mutableMapOf()

        operator fun get(context: Context?): IBindControl {
            return callback ?: BindControl(context).also { callback = it }
        }
    }

}