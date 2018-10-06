package sgtmelon.scriptum.app.model.item;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.view.activity.SplashActivity;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;

public final class StatusItem { // TODO: 02.10.2018 вынести из моделей

    private final Context context;
    private final PendingIntent pendingIntent;

    private NoteItem noteItem;
    private Notification notification;
    private NotificationManager notificationManager;

    // TODO: 29.08.2018 Добавить кнопки к уведомлениям, чтобы была возможность их открепить

    public StatusItem(Context context, NoteItem noteItem, boolean notify) {
        this.context = context;

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(IntentDef.STATUS_OPEN, true);
        intent.putExtra(IntentDef.NOTE_ID, noteItem.getId());

        stackBuilder.addNextIntent(intent);

        pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        updateNote(noteItem, notify);
    }

    //Обновление информации о заметке
    public void updateNote(NoteItem noteItem, boolean notify) {
        this.noteItem = noteItem;

        @DrawableRes int iconId = 0;
        String text = "";

        switch (noteItem.getType()) {
            case TypeDef.text:
                iconId = R.drawable.notif_bind_text;

                text = noteItem.getText();
                break;
            case TypeDef.roll:
                iconId = R.drawable.notif_bind_roll;

                RoomDb db = RoomDb.provideDb(context);
                text = db.daoRoll().getText(noteItem.getId(), noteItem.getText());
                db.close();
                break;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setSmallIcon(iconId)
                .setColor(Help.Clr.get(context, noteItem.getColor(), true))
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
            NotificationChannel notificationChannel = new NotificationChannel(context.getString(R.string.notification_channel_id), context.getString(R.string.notification_channel), NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setSound(null, null);
            notificationChannel.setVibrationPattern(null);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (noteItem.isStatus() && notify) notifyNote();
    }

    //В окне редактирования заметок
    public void updateNote(NoteItem noteItem, List<Long> rkVisible) {
        if (noteItem.isStatus()) {
            Long[] rankId = noteItem.getRankId();
            if (rankId.length == 0 || rkVisible.contains(rankId[0])) {
                updateNote(noteItem, true);
            } else {
                cancelNote();
            }
        }
    }

    //Показывает созданное уведомление
    public void notifyNote() {
        notificationManager.notify((int) noteItem.getId(), notification);
    }

    //Убирает созданное уведомление
    public void cancelNote() {
        notificationManager.cancel((int) noteItem.getId());
    }

}
