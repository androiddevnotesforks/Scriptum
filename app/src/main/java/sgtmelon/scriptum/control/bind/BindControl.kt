package sgtmelon.scriptum.control.bind

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import sgtmelon.scriptum.R
import sgtmelon.scriptum.factory.NotificationFactory
import sgtmelon.scriptum.model.NoteModel

/**
 * Управление закреплением заметки в статус баре [NoteModel]
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
    override fun notify(noteModel: NoteModel, rankIdVisibleList: List<Long>) {
        if (context == null) return

        val id = noteModel.noteEntity.id.toInt()
        val notify = with(noteModel.noteEntity) {
            !isBin && isStatus && isVisible(rankIdVisibleList)
        }

        if (notify) {
            manager?.notify(id, NotificationFactory[context, noteModel])
        } else {
            cancel(id)
        }
    }

    override fun cancel(id: Int) {
        manager?.cancel(id)
    }

    /**
     * Callback which need implement in interface what pass to Interactor
     * It's need to get access [BindControl] inside Interactor
     */
    interface Bridge {
        interface Full : Notify, Cancel

        interface Notify {
            fun notifyBind(noteModel: NoteModel, rankIdVisibleList: List<Long>)
        }

        interface Cancel {
            fun cancelBind(id: Int)
        }
    }

}