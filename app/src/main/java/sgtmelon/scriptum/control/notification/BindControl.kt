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
import sgtmelon.scriptum.control.notification.broadcast.UnbindReceiver
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.office.utils.ColorUtils
import sgtmelon.scriptum.repository.BindRepo
import sgtmelon.scriptum.screen.view.SplashActivity.Companion.getSplashIntent

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
        val icon: Int
        val title = with(noteItem) { if (name.isEmpty()) context.getString(R.string.hint_view_name) else name }
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
                text = rollList.toStatusText(noteItem.text)
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
                .setColor(ColorUtils.get(context, noteItem.color, needDark = true))
                .setContentTitle(title)
                .setContentText(text)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(context.getNotePendingIntent(noteItem))
                .setAutoCancel(false)
                .setOngoing(true)
                .addAction(0, "Unbind", context.getUnbindPendingIntent(noteItem))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setGroup(context.getString(R.string.notification_group))
        }

        return notificationBuilder.build()
    }

    /**
     * В окне редактирования заметок, [rankVisibleList] - id видимых категорий
     */
    fun updateBind(rankVisibleList: List<Long>) = with(noteItem) {
        if (!isStatus) return

        if (rankId.isEmpty() || rankVisibleList.contains(rankId[0])) notifyBind() else cancelBind()
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
        private fun List<RollItem>.toStatusText(checkCount: String) =
                joinToString(prefix = "$checkCount\n", separator = "\n") {
                    "${if (it.isCheck) "\u25CF" else "\u25CB"} ${it.text}"
                }

        private fun Context.getNotePendingIntent(noteItem: NoteItem): PendingIntent? =
                with(TaskStackBuilder.create(this)) {
                    addNextIntent(getSplashIntent(noteItem))
                    return@with getPendingIntent(noteItem.id.toInt(), PendingIntent.FLAG_UPDATE_CURRENT)
                }


        fun Context.getUnbindPendingIntent(noteItem: NoteItem): PendingIntent {
            val intent = Intent(this, UnbindReceiver::class.java).apply {
                putExtra(UnbindReceiver.NOTE_ID_KEY, noteItem.id)
            }

            return PendingIntent.getBroadcast(
                    this, noteItem.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

}