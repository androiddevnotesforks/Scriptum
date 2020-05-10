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
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.extension.removeAtOrNull
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
    override fun notifyNote(noteItem: NoteItem, rankIdVisibleList: List<Long>, @Sort sort: Int) {
        if (context == null) return

        val index = noteItemList.indexOfFirst { it.id == noteItem.id }
        if (index != -1) {
            noteItemList[index] = noteItem
        } else {
            noteItemList.add(noteItem)
        }

        notifyNote(noteItemList, rankIdVisibleList, sort)
    }

    /**
     * If [sort] is null when don't need sort.
     * If [rankIdVisibleList] is null when don't need filter list by visibility.
     */
    override fun notifyNote(itemList: List<NoteItem>, rankIdVisibleList: List<Long>?,
                            @Sort sort: Int?) {
        if (context == null) return

        clearRecent(Tag.NOTE)

        noteItemList.clearAdd(itemList.filter {
            val isVisible = rankIdVisibleList?.let { list -> it.isVisible(list) } ?: true

            return@filter !it.isBin && it.isStatus && isVisible
        }.sort(sort))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (noteItemList.size > 1) {
                val summaryNotification = NotificationFactory.getBingSummary(context)

                manager?.notify(Tag.NOTE_GROUP, Id.NOTE_GROUP, summaryNotification)
                tagIdMap[Tag.NOTE_GROUP] = Id.NOTE_GROUP
            } else {
                clearRecent(Tag.NOTE_GROUP)
            }
        }

        noteItemList.reversed().forEach {
            val id = it.id.toInt()

            manager?.notify(Tag.NOTE, id, NotificationFactory.getBind(context, it))
            tagIdMap[Tag.NOTE] = id
        }
    }

    override fun cancelNote(id: Long) {
        with(noteItemList) {
            removeAtOrNull(indexOfFirst { it.id == id }) ?: return
        }

        notifyNote(noteItemList)
    }

    override fun notifyInfo(count: Int) {
        if (context == null) return

        if (count != 0) {
            manager?.notify(Tag.INFO, Id.INFO, NotificationFactory.getInfo(context, count))
            tagIdMap[Tag.INFO] = Id.INFO
        } else {
            manager?.cancel(Tag.INFO, Id.INFO)
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
            fun notifyNoteBind(item: NoteItem, rankIdVisibleList: List<Long>, @Sort sort: Int)
        }

        interface NotifyAll {
            fun notifyNoteBind(itemList: List<NoteItem>, rankIdVisibleList: List<Long>,
                               @Sort sort: Int? = null)
        }

        interface Cancel {
            fun cancelNoteBind(id: Long)
        }
    }

    /**
     * Use when needs get access to [notifyInfo] inside Interactor
     */
    interface InfoBridge {
        fun notifyInfoBind(count: Int)
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
     * Id's for [Tag.NOTE_GROUP] and [Tag.INFO] notifications.
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