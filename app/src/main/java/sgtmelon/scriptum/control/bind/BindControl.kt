package sgtmelon.scriptum.control.bind

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
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
            manager?.createNotificationChannel(NotificationChannel(
                    context.getString(R.string.notification_channel_id), context.getString(R.string.notification_channel), NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setSound(null, null)
                vibrationPattern = null
            })
        }
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
        } else {
            manager?.cancel(TAG_INFO, INFO_ID)
        }
    }


    /**
     * Callback which need implement in interface what pass to Interactor
     * Use when needs get access to [notifyNote] and [cancelNote] inside Interactor
     */
    interface NoteBridge {
        interface Full : Notify, Cancel

        interface Notify {
            fun notifyNoteBind(noteItem: NoteItem, rankIdVisibleList: List<Long>)
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
    }

}