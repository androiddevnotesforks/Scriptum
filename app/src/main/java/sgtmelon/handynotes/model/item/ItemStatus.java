package sgtmelon.handynotes.model.item;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Arrays;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.db.DbRoom;
import sgtmelon.handynotes.model.state.StateNote;
import sgtmelon.handynotes.db.DbDesc;
import sgtmelon.handynotes.Help;
import sgtmelon.handynotes.ui.act.ActNote;

public class ItemStatus {

    //region Variables
    private final Context context;
    private ItemNote itemNote;

    private final PendingIntent pendingIntent;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManager;
    //endregion

    //TODO: разберись с флагами, то как они работают

    public ItemStatus(Context context, ItemNote itemNote, String[] rkVisible) {
        this.context = context;

        Intent intent = new Intent(context, ActNote.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        intent = itemNote.fillIntent(intent);
        intent.putExtra(DbDesc.RK_VS, rkVisible);
        intent.putExtra(StateNote.KEY_CREATE, false);

//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(ActNote.class);
//        stackBuilder.addNextIntent(intent);

        pendingIntent = PendingIntent.getActivity(context, itemNote.getId(), intent, 0);
//        pendingIntent = stackBuilder.getPendingIntent(itemNote.getId(), PendingIntent.FLAG_UPDATE_CURRENT);

        updateNote(itemNote);
    }

    //Обновление информации о заметке
    public void updateNote(ItemNote itemNote) {
        this.itemNote = itemNote;

        int icon = 0;
        String text = "";
        switch (itemNote.getType()) {
            case DbDesc.typeText:
                icon = R.drawable.ic_menu_bind_text;

                text = itemNote.getText();
                break;
            case DbDesc.typeRoll:
                icon = R.drawable.ic_menu_bind_roll;

                DbRoom db = DbRoom.provideDb(context);
                text = db.daoRoll().getText(itemNote.getCreate(), itemNote.getText());
                db.close();
                break;
        }

        notificationBuilder = new NotificationCompat.Builder(context, context.getString(R.string.channel_status_bind))
                .setSmallIcon(icon)
                .setColor(Help.Icon.getColor(context, true, itemNote.getColor()))
                .setContentTitle(Help.Note.getName(context, itemNote.getName()))
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

    //Обновление отображения в зависимоти от видимости категорий
    //В окне со всеми заметками
    public void updateNote(ItemRank itemRank) {
        String rkId = Integer.toString(itemRank.getId());
        String[] rankId = itemNote.getRankId();

        if (rankId[0].equals(rkId)) {
            if (itemRank.isVisible()) notifyNote();
            else cancelNote();
        }
    }

    //В окне редактирования заметок
    public void updateNote(ItemNote itemNote, String[] rkVisible) {
        if (itemNote.isStatus()) {
            String[] rankId = itemNote.getRankId();
            if (rankId.length == 0 || Arrays.asList(rkVisible).contains(rankId[0])) {
                updateNote(itemNote);
            } else {
                cancelNote();
            }
        }
    }

    //Показывает созданное уведомление
    public void notifyNote() {
        notificationManager.notify(itemNote.getId(), notificationBuilder.build());
    }

    //Убирает созданное уведомление
    public void cancelNote() {
        notificationManager.cancel(itemNote.getId());
    }

}
