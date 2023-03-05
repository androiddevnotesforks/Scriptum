package sgtmelon.scriptum.cleanup.presentation.factory

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import sgtmelon.extensions.getNotificationService
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.getAppSimpleColor
import sgtmelon.scriptum.cleanup.presentation.control.system.BindDelegatorImpl
import sgtmelon.scriptum.infrastructure.model.key.ColorShade
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.receiver.action.UnbindActionReceiver
import sgtmelon.scriptum.infrastructure.screen.splash.SplashActivity
import sgtmelon.scriptum.infrastructure.service.EternalService
import sgtmelon.scriptum.infrastructure.utils.extensions.note.hideChecked
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type
import sgtmelon.test.prod.RunPrivate

/**
 * Factory for create notifications
 */
object NotificationFactory {

    @RequiresApi(VERSION_CODES.O)
    fun deleteOldChannels(context: Context) {
        val service = context.getNotificationService()

        val id = context.getString(R.string.notification_old_channel_id)
        service.deleteNotificationChannel(id)

        val infoId = context.getString(R.string.notification_old_info_channel_id)
        service.deleteNotificationChannel(infoId)
    }

    object Notes {

        @RequiresApi(VERSION_CODES.O)
        fun createChannel(context: Context) {
            context.getNotificationService().createNotificationChannel(getChannel(context))
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
         * Model for [BindDelegatorImpl.notifyNotes]
         *
         * Don't care about [NoteItem.Roll.list] if:
         * - If note type is [NoteType.TEXT]
         * - If type is [NoteType.ROLL] and [NoteItem.Roll.list] is completely load
         * - If you need only call [BindDelegatorImpl.cancelNote]
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
                .addNextIntent(SplashActivity.getBind(context, noteItem))
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
        @RunPrivate fun getStatusTitle(
            context: Context,
            item: NoteItem
        ): String = with(item) {
            val titleStart = if (type == NoteType.ROLL) "$text | " else ""

            return titleStart.plus(name.ifEmpty { context.getString(R.string.empty_note_name) })
        }

        @RunPrivate fun getStatusText(context: Context, item: NoteItem): String {
            return when (item) {
                is NoteItem.Text -> item.text
                is NoteItem.Roll -> {
                    val finalList = if (item.isVisible) item.list else item.list.hideChecked()
                    // TODO первое условие тут точно нужно?
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
        fun createChannel(context: Context) {
            context.getNotificationService().createNotificationChannel(getChannel(context))
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
                .addNextIntent(SplashActivity.getNotification(context))
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
        fun createChannel(context: Context) {
            context.getNotificationService().createNotificationChannel(getChannel(context))
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
            val contentIntent = TaskStackBuilder.create(context)
                .addNextIntent(SplashActivity.getHelpDisappear(context))
                .getPendingIntent(ID, PendingIntent.FLAG_UPDATE_CURRENT)

            val text = context.getString(R.string.notification_eternal_description)

            return NotificationCompat.Builder(context, context.getString(R.string.notification_eternal_channel_id))
                .setSmallIcon(R.drawable.notif_app)
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