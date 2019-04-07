package sgtmelon.scriptum.app.model.item

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.repository.RoomRepo
import sgtmelon.scriptum.app.screen.view.SplashActivity
import sgtmelon.scriptum.office.utils.ColorUtils

/**
 * Управление закреплением заметки в статус баре [NoteModel]
 *
 * @author SerjantArbuz
 */
class StatusItem(private val context: Context, noteItem: NoteItem, notify: Boolean) {

    // TODO: 29.08.2018 Добавить кнопки к уведомлениям, чтобы была возможность их открепить

    private val pendingIntent: PendingIntent? = TaskStackBuilder.create(context).apply {
        addNextIntent(SplashActivity.getIntent(context, noteItem.type, noteItem.id))
    }.getPendingIntent(noteItem.id.toInt(), PendingIntent.FLAG_UPDATE_CURRENT)

    private lateinit var noteItem: NoteItem

    private lateinit var notification: Notification
    private val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        updateNote(noteItem, notify)
    }

    /**
     * Обновление информации о заметке, [notify] - обновление в статус баре
     */
    fun updateNote(noteItem: NoteItem, notify: Boolean = true) {
        this.noteItem = noteItem

        var icon = 0
        var text = ""

        when (noteItem.type) {
            NoteType.TEXT -> {
                icon = R.drawable.notif_bind_text
                text = noteItem.text
            }
            NoteType.ROLL -> {
                icon = R.drawable.notif_bind_roll
                text = RoomRepo.getInstance(context).getRollStatusString(noteItem) //  TODO !!
            }
        }

        val notificationBuilder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setSmallIcon(icon)
                .setColor(ColorUtils.get(context, noteItem.color, true))
                .setContentTitle(noteItem.getStatusName(context))
                .setContentText(text)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setGroup(context.getString(R.string.notification_group))
        }

        notification = notificationBuilder.build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel(
                    context.getString(R.string.notification_channel_id), context.getString(R.string.notification_channel), NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setSound(null, null)
                vibrationPattern = null
            })
        }

        if (noteItem.isStatus && notify) {
            notifyNote()
        }
    }

    /**
     * В окне редактирования заметок, [listRankVisible] - id видимых категорий
     */
    fun updateNote(noteItem: NoteItem, listRankVisible: List<Long>) {
        if (!noteItem.isStatus) return

        val rankId = noteItem.rankId
        if (rankId.isEmpty() || listRankVisible.contains(rankId[0])) {
            updateNote(noteItem)
        } else {
            cancelNote()
        }
    }

    /**
     * Показывает созданное уведомление
     */
    fun notifyNote() = notificationManager.notify(noteItem.id.toInt(), notification)

    /**
     * Убирает созданное уведомление
     */
    fun cancelNote() = notificationManager.cancel(noteItem.id.toInt())

}
