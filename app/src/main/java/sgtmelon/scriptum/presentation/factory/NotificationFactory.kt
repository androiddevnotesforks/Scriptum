package sgtmelon.scriptum.presentation.factory

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.test.RunPrivate
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RollItem
import sgtmelon.scriptum.domain.model.key.ColorShade
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.extension.getAppSimpleColor
import sgtmelon.scriptum.extension.getNotificationService
import sgtmelon.scriptum.extension.hide
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.receiver.action.UnbindActionReceiver
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.service.EternalService

/**
 * Factory for create notifications
 */
object NotificationFactory {

    @RequiresApi(VERSION_CODES.O)
    fun deleteOldChannel(context: Context?) {
        if (context == null) return

        val id = context.getString(R.string.notification_old_channel_id)
        context.getNotificationService()?.deleteNotificationChannel(id)

        val infoId = context.getString(R.string.notification_old_info_channel_id)
        context.getNotificationService()?.deleteNotificationChannel(infoId)
    }

    object Notes {

        @RequiresApi(VERSION_CODES.O)
        fun createChannel(context: Context?) {
            if (context == null) return

            context.getNotificationService()?.createNotificationChannel(getChannel(context))
        }

        @RequiresApi(VERSION_CODES.O)
        private fun getChannel(context: Context): NotificationChannel {
            val id = context.getString(R.string.notification_notes_channel_id)
            val name = context.getString(R.string.notification_notes_channel_title)
            val description = context.getString(R.string.notification_notes_channel_description)

            return NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                setSound(null, null)
                this.vibrationPattern = null
                this.description = description
            }
        }

        /**
         * Model for [BindControl.notifyNotes]
         *
         * Don't care about [NoteItem.Roll.list] if:
         * - If note type is [NoteType.TEXT]
         * - If type is [NoteType.ROLL] and [NoteItem.Roll.list] is completely load
         * - If you need only call [BindControl.cancelNote]
         */
        operator fun get(context: Context, noteItem: NoteItem): Notification {
            val channelId = context.getString(R.string.notification_notes_channel_id)
            val icon = when (noteItem) {
                is NoteItem.Text -> R.drawable.notif_bind_text
                is NoteItem.Roll -> R.drawable.notif_bind_roll
            }

            val color = context.getAppSimpleColor(noteItem.color, ColorShade.DARK)
            val title = getStatusTitle(context, noteItem)
            val text = getStatusText(context, noteItem)

            val id = noteItem.id.toInt()
            val contentIntent = TaskStackBuilder.create(context)
                .addNextIntent(SplashActivity.getBindInstance(context, noteItem))
                .getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)

            return NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setColor(color)
                .setContentTitle(title)
                .setContentText(text)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .addAction(0, context.getString(R.string.notification_button_unbind), UnbindActionReceiver[context, noteItem])
                .setAutoCancel(false)
                .setOngoing(true)
                .setGroup(context.getString(R.string.notification_group_notes))
                .build()
        }

        /**
         * If [NoteType.ROLL] - title will starts with amount of done list items.
         */
        @RunPrivate fun getStatusTitle(context: Context, item: NoteItem): String = with(item) {
            val titleStart = if (type == NoteType.ROLL) "$text | " else ""

            return titleStart.plus(if (name.isEmpty()) context.getString(R.string.hint_text_name) else name)
        }

        @RunPrivate fun getStatusText(context: Context, item: NoteItem): String {
            return when (item) {
                is NoteItem.Text -> item.text
                is NoteItem.Roll -> {
                    val finalList = if (item.isVisible) item.list else item.list.hide()
                    if (item.isVisible || finalList.isNotEmpty()) {
                        finalList.toStatusText()
                    } else {
                        context.getString(R.string.info_roll_hide_title)
                    }
                }
            }
        }

        private fun List<RollItem>.toStatusText() = joinToString(separator = "\n") {
            "${if (it.isCheck) "\u25CF" else "\u25CB"} ${it.text}"
        }

        @RequiresApi(VERSION_CODES.N)
        fun getBindSummary(context: Context): Notification {
            return NotificationCompat.Builder(context, context.getString(R.string.notification_notes_channel_id))
                .setSmallIcon(R.drawable.notif_bind_group)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setOngoing(true)
                .setGroup(context.getString(R.string.notification_group_notes))
                .setGroupSummary(true)
                .build()
        }
    }

    object Count {

        @RequiresApi(VERSION_CODES.O)
        fun createChannel(context: Context?) {
            if (context == null) return

            context.getNotificationService()?.createNotificationChannel(getChannel(context))
        }

        @RequiresApi(VERSION_CODES.O)
        private fun getChannel(context: Context): NotificationChannel {
            val id = context.getString(R.string.notification_count_channel_id)
            val name = context.getString(R.string.notification_count_channel_title)
            val description = context.getString(R.string.notification_count_channel_description)

            return NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                setSound(null, null)
                this.vibrationPattern = null
                this.description = description
            }
        }

        /**
         * Notification for display count of alarm.
         */
        operator fun get(context: Context, id: Int, count: Int): Notification {
            val contentIntent = TaskStackBuilder.create(context)
                .addNextIntent(SplashActivity.getNotificationInstance(context))
                .getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT)

            return NotificationCompat.Builder(context, context.getString(R.string.notification_count_channel_id))
                .setSmallIcon(R.drawable.notif_info)
                .setContentTitle(context.resources.getQuantityString(R.plurals.notification_count_title, count, count))
                .setContentText(context.getString(R.string.notification_info_description))
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .setGroup(context.getString(R.string.notification_group_info))
                .build()
        }
    }

    object Service {

        @RequiresApi(VERSION_CODES.O)
        fun createChannel(context: Context?) {
            if (context == null) return

            context.getNotificationService()?.createNotificationChannel(getChannel(context))
        }

        @RequiresApi(VERSION_CODES.O)
        private fun getChannel(context: Context): NotificationChannel {
            val id = context.getString(R.string.notification_eternal_channel_id)
            val name = context.getString(R.string.notification_eternal_channel_title)
            val description = context.getString(R.string.notification_eternal_channel_description)

            /**
             * [NotificationManager.IMPORTANCE_HIGH] need for prevent closing of [EternalService].
             */
            return NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH).apply {
                setSound(null, null)
                this.vibrationPattern = null
                this.description = description
            }
        }

        /**
         * Notification for good work of [EternalService].
         */
        operator fun get(context: Context): Notification {
            /**
             * Intent for open application settings on tap.
             */
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", context.packageName, null)

            val contentIntent = TaskStackBuilder.create(context)
                .addNextIntent(intent)
                .getPendingIntent(ID, PendingIntent.FLAG_UPDATE_CURRENT)

            val text = context.getString(R.string.notification_eternal_description)

            // TODO change icon it looks ugly
            return NotificationCompat.Builder(context, context.getString(R.string.notification_eternal_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.notification_eternal_title))
                .setContentText(text)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .setGroup(context.getString(R.string.notification_group_eternal))
                .build()
        }

        const val ID = 1
        const val REQUEST_CODE = 1
    }
}