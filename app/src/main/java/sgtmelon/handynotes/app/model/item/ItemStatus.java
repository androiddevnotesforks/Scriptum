package sgtmelon.handynotes.app.model.item;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

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
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManager;
    //endregion

    // FIXME: 04.07.2018 Не работает привязка к статусбару на эмуляторе и на ранних версиях андроид
    //TODO: разберись с флагами, то как они работают

    public ItemStatus(Context context, ItemNote itemNote) {
        this.context = context;

        Intent intent = new Intent(context, ActNote.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        intent.putExtra(Db.NT_ID, itemNote.getId());
        intent.putExtra(DefPage.CREATE, false);
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(ActNote.class);
//        stackBuilder.addNextIntent(intent);

        pendingIntent = PendingIntent.getActivity(context, (int) itemNote.getId(), intent, 0);
//        pendingIntent = stackBuilder.getPendingIntent(itemNote.getId(), PendingIntent.FLAG_UPDATE_CURRENT);

        updateNote(itemNote);
    }

    //Обновление информации о заметке
    public void updateNote(ItemNote itemNote) {
        this.itemNote = itemNote;

        int icon = 0;
        String text = "";

        switch (itemNote.getType()) {
            case DefType.text:
                icon = R.drawable.ic_bind_text;

                text = itemNote.getText();
                break;
            case DefType.roll:
                icon = R.drawable.ic_bind_roll;

                DbRoom db = DbRoom.provideDb(context);
                text = db.daoRoll().getText(itemNote.getId(), itemNote.getText());
                db.close();
                break;
        }

        notificationBuilder = new NotificationCompat.Builder(context, context.getString(R.string.channel_status_bind))
                .setSmallIcon(icon)
                .setColor(Help.Icon.getColor(context, true, itemNote.getColor()))
                .setContentTitle(itemNote.getName(context))
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setGroup(context.getString(R.string.group_status_bind))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOngoing(true);

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
        notificationManager.notify((int) itemNote.getId(), notificationBuilder.build());
    }

    //Убирает созданное уведомление
    public void cancelNote() {
        notificationManager.cancel((int) itemNote.getId());
    }

}
