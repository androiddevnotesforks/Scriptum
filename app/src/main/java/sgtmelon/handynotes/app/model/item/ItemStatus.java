package sgtmelon.handynotes.app.model.item;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.app.view.act.ActMain;
import sgtmelon.handynotes.app.view.act.ActNote;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.DefPage;
import sgtmelon.handynotes.office.annot.def.db.DefType;

public class ItemStatus {

    //region Variables
    private static final String TAG = "ItemStatus";

    private final Context context;
    private ItemNote itemNote;

    private final PendingIntent pendingIntent;
    private Notification notification;
    private NotificationManager notificationManager;
    //endregion

    // TODO: 29.08.2018 Добавить кнопки к уведомлениям, чтобы была возможность их открепить

    public ItemStatus(Context context, ItemNote itemNote) {
        this.context = context;

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ActMain.class);

        Intent intent = new Intent(context, ActNote.class);
        intent.putExtra(Db.NT_ID, itemNote.getId());
        intent.putExtra(DefPage.CREATE, false);

        stackBuilder.addNextIntent(intent);

        pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        updateNote(itemNote);
    }

    //Обновление информации о заметке
    public void updateNote(ItemNote itemNote) {
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
                .setColor(Help.Col.get(context, itemNote.getColor(), true))
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

        if (itemNote.isStatus()) notifyNote();
    }

    //В окне редактирования заметок
    public void updateNote(ItemNote itemNote, List<Long> rkVisible) {
        if (itemNote.isStatus()) {
            Long[] rankId = itemNote.getRankId();
            if (rankId.length == 0 || rkVisible.contains(rankId[0])) {
                updateNote(itemNote);
            } else {
                cancelNote();
            }
        }
    }

    //Показывает созданное уведомление
    public void notifyNote() {
        Log.i(TAG, "notifyNote");

        notificationManager.notify((int) itemNote.getId(), notification);
    }

    //Убирает созданное уведомление
    public void cancelNote() {
        Log.i(TAG, "cancelNote");

        notificationManager.cancel((int) itemNote.getId());
    }

}
