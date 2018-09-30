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
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.view.act.ActSplash;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.st.StNote;

public class ItemStatus {

    private final Context context;
    private final PendingIntent pendingIntent;

    private ItemNote itemNote;
    private Notification notification;
    private NotificationManager notificationManager;

    // TODO: 29.08.2018 Добавить кнопки к уведомлениям, чтобы была возможность их открепить

    public ItemStatus(Context context, ItemNote itemNote, boolean notify) {
        this.context = context;

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        Intent intent = new Intent(context, ActSplash.class);
        intent.putExtra(DefIntent.STATUS_OPEN, true);
        intent.putExtra(DefIntent.STATE_NOTE, new StNote(false, false));
        intent.putExtra(DefIntent.NOTE_ID, itemNote.getId());

        stackBuilder.addNextIntent(intent);

        pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        updateNote(itemNote, notify);
    }

    //Обновление информации о заметке
    public void updateNote(ItemNote itemNote, boolean notify) {
        this.itemNote = itemNote;

        @DrawableRes int iconId = 0;
        String text = "";

        switch (itemNote.getType()) {
            case DefType.text:
                iconId = R.drawable.ic_bind_text_notif;

                text = itemNote.getText();
                break;
            case DefType.roll:
                iconId = R.drawable.ic_bind_roll_notif;

                DbRoom db = DbRoom.provideDb(context);
                text = db.daoRoll().getText(itemNote.getId(), itemNote.getText());
                db.close();
                break;
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setSmallIcon(iconId)
                .setColor(Help.Clr.get(context, itemNote.getColor(), true))
                .setContentTitle(itemNote.getName(context))
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

        if (itemNote.isStatus() && notify) notifyNote();
    }

    //В окне редактирования заметок
    public void updateNote(ItemNote itemNote, List<Long> rkVisible) {
        if (itemNote.isStatus()) {
            Long[] rankId = itemNote.getRankId();
            if (rankId.length == 0 || rkVisible.contains(rankId[0])) {
                updateNote(itemNote, true);
            } else {
                cancelNote();
            }
        }
    }

    //Показывает созданное уведомление
    public void notifyNote() {
        notificationManager.notify((int) itemNote.getId(), notification);
    }

    //Убирает созданное уведомление
    public void cancelNote() {
        notificationManager.cancel((int) itemNote.getId());
    }

}
