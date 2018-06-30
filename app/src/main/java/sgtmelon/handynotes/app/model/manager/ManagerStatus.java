package sgtmelon.handynotes.app.model.manager;

import android.content.Context;

import java.util.List;

import sgtmelon.handynotes.db.item.ItemNote;
import sgtmelon.handynotes.db.item.ItemRank;
import sgtmelon.handynotes.db.item.ItemStatus;

public class ManagerStatus {

    private final Context context;

    private final List<Long> listId;
    private final List<ItemStatus> listStatus;

    /**
     * Конструктов менеджера статусовых сообщений
     * @param context - Используется для создания нового уведомления
     * @param listId - Список id заметок
     * @param listStatus - Список моделей создания уведомления
     */
    public ManagerStatus(Context context, List<Long> listId, List<ItemStatus> listStatus) {
        this.context = context;

        this.listId = listId;
        this.listStatus = listStatus;
    }

    public void insertItem(ItemNote itemNote, Long[] rankVisible) {
        ItemStatus itemStatus = new ItemStatus(context, itemNote, rankVisible);

        listId.add(itemNote.getId());
        listStatus.add(itemStatus);
    }

    //Обновление при закреплении/откреплении заметки
    public void updateItemBind(ItemNote itemNote) {
        int i = listId.indexOf(itemNote.getId());
        if (i != -1) {
            ItemStatus itemStatus = listStatus.get(i);
            itemStatus.updateNote(itemNote);

            listStatus.set(i, itemStatus);
        }
    }

    //При изменении видимости категории
    public void updateItemVisible(ItemRank itemRank) {
        Long[] rankIdNote = itemRank.getIdNote();

        for (long id : rankIdNote) {
            int i = listId.indexOf(id);
            if (i != -1) {
                ItemStatus itemStatus = listStatus.get(i);
                itemStatus.updateNote(itemRank);
                listStatus.set(i, itemStatus);
            }
        }
    }

    //При изменении видимости нескольких категорий
    public void updateItemVisible(List<ItemRank> listRank) {
        for (ItemRank itemRank : listRank) {
            updateItemVisible(itemRank);
        }
    }

    public void removeItem(ItemNote itemNote) {
        int i = listId.indexOf(itemNote.getId());
        if (i != -1) {
            ItemStatus itemStatus = listStatus.get(i);
            itemStatus.cancelNote();

            listId.remove(i);
            listStatus.remove(i);
        }

    }

}
