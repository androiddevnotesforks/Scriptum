package sgtmelon.scriptum.app.model.item;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.view.activity.SplashActivity;
import sgtmelon.scriptum.office.utils.ColorUtils;

/**
 * Управление закреплением заметки в статус баре {@link NoteRepo}
 */
public final class StatusItem {

    private final Context context;
    private final PendingIntent pendingIntent;

    private NoteItem noteItem;
    private Notification notification;
    private NotificationManager notificationManager;

    // TODO: 29.08.2018 Добавить кнопки к уведомлениям, чтобы была возможность их открепить

    public StatusItem(Context context, NoteItem noteItem, boolean notify) {
        this.context = context;

        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(SplashActivity.Companion.getInstance(context, noteItem.getId()));

        pendingIntent = stackBuilder.getPendingIntent((int) noteItem.getId(), PendingIntent.FLAG_UPDATE_CURRENT);

        updateNote(noteItem, notify);
    }

    /**
     * Обновление информации о заметке
     *
     * @param noteItem - Модель заметки
     * @param notify   - Необходимость обновления
     */
    public void updateNote(NoteItem noteItem, boolean notify) {
        this.noteItem = noteItem;

        int icon = 0;
        String text = "";

        switch (noteItem.getType()) {
            case TEXT:
                icon = R.drawable.notif_bind_text;
                text = noteItem.getText();
                break;
            case ROLL:
                icon = R.drawable.notif_bind_roll;

                final RoomDb db = RoomDb.provideDb(context);
                text = db.daoRoll().getText(noteItem.getId(), noteItem.getText());
                db.close();
                break;
        }

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setSmallIcon(icon)
                .setColor(ColorUtils.INSTANCE.get(context, noteItem.getColor(), true))
                .setContentTitle(noteItem.getName(context))
                .setContentText(text)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setGroup(context.getString(R.string.notification_group));
        }

        notification = notificationBuilder.build();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(context.getString(R.string.notification_channel_id), context.getString(R.string.notification_channel), NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setSound(null, null);
            notificationChannel.setVibrationPattern(null);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (noteItem.isStatus() && notify) {
            notifyNote();
        }
    }

    /**
     * В окне редактирования заметок
     *
     * @param noteItem  - Модель заметки
     * @param rkVisible - Видимые категории
     */
    public void updateNote(NoteItem noteItem, List<Long> rkVisible) {
        if (!noteItem.isStatus()) return;

        final List<Long> rankId = noteItem.getRankId();
        if (rankId.size() == 0 || rkVisible.contains(rankId.get(0))) {
            updateNote(noteItem, true);
        } else {
            cancelNote();
        }
    }

    /**
     * Показывает созданное уведомление
     */
    public void notifyNote() {
        notificationManager.notify((int) noteItem.getId(), notification);
    }

    /**
     * Убирает созданное уведомление
     */
    public void cancelNote() {
        notificationManager.cancel((int) noteItem.getId());
    }

}
