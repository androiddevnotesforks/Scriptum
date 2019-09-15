package sgtmelon.scriptum.control.bind

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.BindModel
import sgtmelon.scriptum.model.NoteModel

/**
 * Управление закреплением заметки в статус баре [NoteModel]
 */
class BindControl(private val context: Context) : IBindControl {

    private val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager?.createNotificationChannel(NotificationChannel(
                    context.getString(R.string.notification_channel_id), context.getString(R.string.notification_channel), NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setSound(null, null)
                vibrationPattern = null
            })
        }
    }

    private fun createNotification(noteModel: NoteModel): Notification {
        val bindModel = BindModel(noteModel)

        val builder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setSmallIcon(bindModel.icon)
                .setColor(bindModel.getColor(context))
                .setContentTitle(bindModel.getTitle(context))
                .setContentText(bindModel.text)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(NotificationCompat.BigTextStyle().bigText(bindModel.text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(bindModel.getContentIntent(context))
                .addAction(0, context.getString(R.string.notification_button_unbind), bindModel.getUnbindIntent(context))
                .setAutoCancel(false)
                .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setGroup(context.getString(R.string.notification_group))
        }

        return builder.build()
    }

    /**
     * Update notification if note isStatus and isVisible, otherwise cancel notification
     */
    override fun notify(noteModel: NoteModel, rankIdVisibleList: List<Long>) {
        val id = noteModel.noteEntity.id.toInt()
        val notify = with(noteModel.noteEntity) {
            !isBin && isStatus && isVisible(rankIdVisibleList)
        }

        if (notify) manager?.notify(id, createNotification(noteModel)) else cancel(id)
    }

    /**
     * Remove notification from statusBar
     */
    override fun cancel(id: Int) {
        manager?.cancel(id)
    }
}