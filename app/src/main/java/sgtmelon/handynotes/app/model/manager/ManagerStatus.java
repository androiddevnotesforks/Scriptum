package sgtmelon.handynotes.app.model.manager;

import android.content.Context;

import java.util.List;

import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRank;
import sgtmelon.handynotes.app.model.item.ItemStatus;

public class ManagerStatus {

    private final Context context;

    private final List<String> listCreate;
    private final List<ItemStatus> listStatus;

    /**
     * Конструктов менеджера статусовых сообщений
     * @param context - Используется для создания нового уведомления
     * @param listCreate - Список дат создания заметок
     * @param listStatus - Список моделей создания уведомления
     */
    public ManagerStatus(Context context, List<String> listCreate, List<ItemStatus> listStatus) {
        this.context = context;

        this.listCreate = listCreate;
        this.listStatus = listStatus;
    }

    public void insertItem(ItemNote itemNote, String[] rkVisible) {
        ItemStatus itemStatus = new ItemStatus(context, itemNote, rkVisible);

        listCreate.add(itemNote.getCreate());
        listStatus.add(itemStatus);
    }

    //Обновление при закреплении/откреплении заметки
    public void updateItemBind(ItemNote itemNote) {
        int index = listCreate.indexOf(itemNote.getCreate());

        if (index != -1) {
            ItemStatus itemStatus = listStatus.get(index);
            itemStatus.updateNote(itemNote);

            listStatus.set(index, itemStatus);
        }
    }

    //При изменении видимости категории
    public void updateItemVisible(ItemRank itemRank) {
        String[] ntCreate = itemRank.getCreate();

        for (String aNtCreate : ntCreate) {
            int index = listCreate.indexOf(aNtCreate);

            if (index != -1) {
                ItemStatus itemStatus = listStatus.get(index);
                itemStatus.updateNote(itemRank);
                listStatus.set(index, itemStatus);
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
        int index = listCreate.indexOf(itemNote.getCreate());

        if (index != -1) {
            ItemStatus itemStatus = listStatus.get(index);
            itemStatus.cancelNote();

            listCreate.remove(index);
            listStatus.remove(index);
        }

    }

}
