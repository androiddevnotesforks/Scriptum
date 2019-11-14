package sgtmelon.scriptum.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.UnbindReceiver
import sgtmelon.scriptum.screen.ui.SplashActivity

/**
 * Factory for create notifications
 */
object NotificationFactory {

    /**
     * Model for [BindControl.notifyNote]
     *
     * Don't care about [NoteItem.rollList] if:
     * - If note type is [NoteType.TEXT]
     * - If type is [NoteType.ROLL] and [NoteItem.rollList] is completely load
     * - If you need only call [BindControl.cancelNote]
     */
    fun getBind(context: Context, noteItem: NoteItem): Notification {
        val icon = when (noteItem.type) {
            NoteType.TEXT -> R.drawable.notif_bind_text
            NoteType.ROLL -> R.drawable.notif_bind_roll
        }

        val color = context.getAppSimpleColor(noteItem.color, ColorShade.DARK)
        val title = noteItem.getStatusTitle(context)
        val text = when (noteItem.type) {
            NoteType.TEXT -> noteItem.text
            NoteType.ROLL -> noteItem.rollList.toStatusText()
        }

        val id = noteItem.id.toInt()
        val contentIntent = TaskStackBuilder.create(context)
                .addNextIntent(SplashActivity.getBindInstance(context, noteItem))
                .getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setSmallIcon(icon)
                .setColor(color)
                .setContentTitle(title)
                .setContentText(text)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .addAction(0, context.getString(R.string.notification_button_unbind), UnbindReceiver[context, noteItem])
                .setAutoCancel(false)
                .setOngoing(true)

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            builder.setGroup(context.getString(R.string.notification_group_notes))
        }

        return builder.build()
    }

    private fun NoteItem.getStatusTitle(context: Context): String {
        return (if (type == NoteType.ROLL) "$text | " else "")
                .plus(if (name.isEmpty()) context.getString(R.string.hint_text_name) else name)
    }

    private fun List<RollItem>.toStatusText() = joinToString(separator = "\n") {
        "${if (it.isCheck) "\u25CF" else "\u25CB"} ${it.text}"
    }


    fun getInfo(context: Context, count: Int): Notification {
        val contentIntent = TaskStackBuilder.create(context)
                .addNextIntent(SplashActivity.getNotificationInstance(context))
                .getPendingIntent(BindControl.INFO_ID, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setSmallIcon(R.drawable.notif_info)
                .setContentTitle(context.resources.getQuantityString(R.plurals.notification_info_title, count, count))
                .setContentText(context.getString(R.string.notification_info_description))
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setAutoCancel(false)
                .setOngoing(true)

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            builder.setGroup(context.getString(R.string.notification_group_info))
        }

        return builder.build()
    }

}