package sgtmelon.scriptum.factory

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.bind.BindControl
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.ColorShade
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.UnbindReceiver
import sgtmelon.scriptum.room.entity.RollEntity
import sgtmelon.scriptum.screen.ui.SplashActivity

/**
 * Factory for create notifications
 */
object NotificationFactory {

    /**
     * Model for [BindControl]
     *
     * Don't care about [NoteModel.rollList] if:
     * - If note type is [NoteType.TEXT]
     * - If type is [NoteType.ROLL] and [NoteModel.rollList] is completely load
     * - If you need only call [BindControl.cancelNote]
     */
    operator fun get(context: Context, noteModel: NoteModel): Notification {
        val icon = when (noteModel.noteEntity.type) {
            NoteType.TEXT -> R.drawable.notif_bind_text
            NoteType.ROLL -> R.drawable.notif_bind_roll
        }

        val color = context.getAppSimpleColor(noteModel.noteEntity.color, ColorShade.DARK)
        val title = noteModel.getStatusTitle(context)
        val text = when (noteModel.noteEntity.type) {
            NoteType.TEXT -> noteModel.noteEntity.text
            NoteType.ROLL -> noteModel.rollList.toStatusText()
        }

        val id = noteModel.noteEntity.id.toInt()
        val contentIntent = TaskStackBuilder.create(context)
                .addNextIntent(SplashActivity.getBindInstance(context, noteModel.noteEntity))
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
                .addAction(0, context.getString(R.string.notification_button_unbind), UnbindReceiver[context, noteModel.noteEntity])
                .setAutoCancel(false)
                .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setGroup(context.getString(R.string.notification_group))
        }

        return builder.build()
    }

    private fun NoteModel.getStatusTitle(context: Context): String = with(noteEntity) {
        (if (type == NoteType.ROLL) "$text | " else "")
                .plus(if (name.isEmpty()) context.getString(R.string.hint_text_name) else name)
    }

    private fun List<RollEntity>.toStatusText() = joinToString(separator = "\n") {
        "${if (it.isCheck) "\u25CF" else "\u25CB"} ${it.text}"
    }

}