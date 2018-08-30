package sgtmelon.handynotes.app.model.item;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.dataBase.DbRoom;
import sgtmelon.handynotes.app.view.act.ActNote;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.DefPage;
import sgtmelon.handynotes.office.annot.def.db.DefType;

public class ItemStatus {

    //region Variables
    private final Context context;
    private ItemNote itemNote;

    private final PendingIntent pendingIntent;
    private Notification notification;
    private NotificationManagerCompat notificationManager;
    //endregion

    //TODO: разберись с флагами, то как они работают
    // TODO: 29.08.2018 Добавить кнопки к уведомлениям, чтобы была возможность их открепить

    public ItemStatus(Context context, ItemNote itemNote) {
        this.context = context;

        Intent intent = new Intent(context, ActNote.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        intent.putExtra(Db.NT_ID, itemNote.getId());
        intent.putExtra(DefPage.CREATE, false);

        pendingIntent = PendingIntent.getActivity(context, (int) itemNote.getId(), intent, 0);

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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, context.getString(R.string.channel_status_bind))
                .setSmallIcon(iconId)
                .setColor(Help.Col.get(context, itemNote.getColor(), true))
                .setContentTitle(itemNote.getName(context))
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text)) // TODO: 29.08.2018 Если маленький текст, то всё равно увеличивается до большого уведомления
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // TODO: 29.08.2018 Подумай над отображением их в правильном порядке после запуска приложения и во время работы (чтобы позиции соответствовали действительности, а не в обратном порядке)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setGroup(context.getString(R.string.group_status_bind));
        }

        notification = notificationBuilder.build();
        notificationManager = NotificationManagerCompat.from(context);

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
        notificationManager.notify((int) itemNote.getId(), notification);
    }

    //Убирает созданное уведомление
    public void cancelNote() {
        notificationManager.cancel((int) itemNote.getId());
    }

}
