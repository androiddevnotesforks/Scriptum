package sgtmelon.scriptum.control.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.model.key.ReceiverKey
import sgtmelon.scriptum.receiver.BindReceiver
import sgtmelon.scriptum.repository.bind.BindRepo
import sgtmelon.scriptum.repository.room.RoomRepo.Companion.isVisible
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashBindIntent

/**
 * Управление закреплением заметки в статус баре [NoteModel]
 *
 * @author SerjantArbuz
 */
class BindControl(private val context: Context, noteModel: NoteModel) {

    /**
     * Конструктор, на случай, если нет списка пунктов для уведомления или он не нужен
     */
    constructor(context: Context, noteItem: NoteItem) : this(context, NoteModel(noteItem))

    private val iBindRepo = BindRepo.getInstance(context)

    private val noteItem: NoteItem = noteModel.noteItem

    private val notification: Notification

    private val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        val title = with(noteItem) {
            "${if(type == NoteType.ROLL) "$text |" else ""} ${if (name.isEmpty()) context.getString(R.string.hint_view_name) else name}"
        }

        val icon: Int
        val text: String

        when (noteItem.type) {
            NoteType.TEXT -> {
                icon = R.drawable.notif_bind_text
                text = noteItem.text
            }
            NoteType.ROLL -> {
                val rollList = with(noteModel.rollList) {
                    if (isNotEmpty()) this else iBindRepo.getRollList(noteItem)
                }

                icon = R.drawable.notif_bind_roll
                text = rollList.toStatusText()
            }
        }

        notification = createNotification(icon, title, text)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel(
                    context.getString(R.string.notification_channel_id), context.getString(R.string.notification_channel), NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setSound(null, null)
                vibrationPattern = null
            })
        }
    }

    private fun createNotification(icon: Int, title: String, text: String): Notification {
        val notificationBuilder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setSmallIcon(icon)
                .setColor(context.getAppSimpleColor(noteItem.color, ColorShade.DARK))
                .setContentTitle(title)
                .setContentText(text)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(context.getNotePendingIntent(noteItem))
                .addAction(0, context.getString(R.string.notification_button_unbind), context.getUnbindPendingIntent(noteItem))
                .setAutoCancel(false)
                .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setGroup(context.getString(R.string.notification_group))
        }

        return notificationBuilder.build()
    }

    /**
     * Обновление уведомления, если оно закреплено и заметка не скрыта
     * [rankIdVisibleList] - Id видимых категорий
     */
    fun updateBind(rankIdVisibleList: List<Long>) = with(noteItem) {
        if (!isStatus) return

        if (isVisible(rankIdVisibleList)) notifyBind() else cancelBind()
    }

    fun updateBind() = if (noteItem.isStatus) notifyBind() else cancelBind()

    /**
     * Показывает созданное уведомление
     */
    fun notifyBind() = notificationManager.notify(noteItem.id.toInt(), notification)

    /**
     * Убирает созданное уведомление
     */
    fun cancelBind() = notificationManager.cancel(noteItem.id.toInt())

    companion object {
        private fun List<RollItem>.toStatusText() = joinToString(separator = "\n") {
            "${if (it.isCheck) "\u25CF" else "\u25CB"} ${it.text}"
        }

        private fun Context.getNotePendingIntent(noteItem: NoteItem): PendingIntent? =
                with(TaskStackBuilder.create(this)) {
                    addNextIntent(getSplashBindIntent(noteItem))
                    return@with getPendingIntent(noteItem.id.toInt(), PendingIntent.FLAG_UPDATE_CURRENT)
                }


        fun Context.getUnbindPendingIntent(noteItem: NoteItem): PendingIntent {
            val intent = Intent(this, BindReceiver::class.java).apply {
                putExtra(ReceiverKey.Values.NOTE_ID, noteItem.id)
            }

            return PendingIntent.getBroadcast(
                    this, noteItem.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

}