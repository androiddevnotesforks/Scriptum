package sgtmelon.scriptum.control.notification

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
class BindControl(private val context: Context) {

    private val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

    private var notification: Notification? = null
    private var bindModel: BindModel? = null

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

    fun setup(noteModel: NoteModel) = setup(BindModel(noteModel))

    private fun setup(bindModel: BindModel) = apply {
        this.bindModel = bindModel

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

        notification = builder.build()
    }

    /**
     * Update notification if it isStatus and isVisible
     */
    fun updateBind(rankIdVisibleList: List<Long>) {
        val bindModel = bindModel ?: return

        if (!bindModel.isStatus) return

        if (bindModel.isVisible(rankIdVisibleList)) notifyBind() else cancelBind()
    }

    fun updateBind() {
        val bindModel = bindModel ?: return

        if (bindModel.isStatus) notifyBind() else cancelBind()
    }

    /**
     * Show notification in statusBar
     */
    fun notifyBind() {
        val bindModel = bindModel ?: return

        manager?.notify(bindModel.id, notification)
    }

    /**
     * Remove notification from statusBar
     */
    fun cancelBind() {
        val bindModel = bindModel ?: return

        manager?.cancel(bindModel.id)
    }

}