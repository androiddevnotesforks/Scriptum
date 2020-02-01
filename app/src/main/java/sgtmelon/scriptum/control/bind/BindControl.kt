package sgtmelon.scriptum.control.bind

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.factory.NotificationFactory
import sgtmelon.scriptum.model.item.NoteItem

/**
 * Class for help control [NoteItem] notification bind in statusBar
 */
class BindControl(private val context: Context?) : IBindControl {

    private val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE)
            as? NotificationManager

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
    override fun notifyNote(noteItem: NoteItem, rankIdVisibleList: List<Long>) {
        if (context == null) return

        val id = noteItem.id.toInt()
        val notify = with(noteItem) {
            !isBin && isStatus && isVisible(rankIdVisibleList)
        }

        if (notify) {
            manager?.notify(TAG_NOTE, id, NotificationFactory.getBind(context, noteItem))

            if (BuildConfig.DEBUG) {
                tagIdMap[TAG_NOTE] = id
            }
        } else {
            cancelNote(id)
        }
    }

    override fun cancelNote(id: Int) {
        manager?.cancel(TAG_NOTE, id)
    }

    override fun notifyInfo(count: Int) {
        if (context == null) return

        if (count != 0) {
            manager?.notify(TAG_INFO, INFO_ID, NotificationFactory.getInfo(context, count))

            if (BuildConfig.DEBUG) {
                tagIdMap[TAG_INFO] = INFO_ID
            }
        } else {
            manager?.cancel(TAG_INFO, INFO_ID)
        }
    }

    override fun clear() {
        if (BuildConfig.DEBUG) {
            tagIdMap.forEach { manager?.cancel(it.key, it.value) }
            tagIdMap.clear()
        }
    }


    /**
     * Callback which need implement in interface what pass to Interactor
     * Use when needs get access to [notifyNote] and [cancelNote] inside Interactor
     */
    interface NoteBridge {
        interface Full : Notify, Cancel

        interface Notify {
            fun notifyNoteBind(item: NoteItem, rankIdVisibleList: List<Long>)
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

    companion object {
        private const val PREFIX = "TAG_BIND"

        private const val TAG_NOTE = "${PREFIX}_NOTE"
        private const val TAG_INFO = "${PREFIX}_INFO"

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