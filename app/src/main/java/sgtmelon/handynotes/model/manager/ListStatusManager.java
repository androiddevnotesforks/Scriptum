package sgtmelon.handynotes.model.manager;

import android.content.Context;

import java.util.List;

import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemRank;
import sgtmelon.handynotes.model.item.ItemStatus;

public class ListStatusManager {

    private Context context;

    private List<String> listNoteCreate;
    private List<ItemStatus> listStatus;

    public ListStatusManager(Context context, List<String> listNoteCreate, List<ItemStatus> listStatus) {
        this.context = context;

        this.listNoteCreate = listNoteCreate;
        this.listStatus = listStatus;
    }

    public void insertItem(ItemNote itemNote, String[] rkVisible) {
        ItemStatus itemStatus = new ItemStatus(context, itemNote, rkVisible);

        listNoteCreate.add(itemNote.getCreate());
        listStatus.add(itemStatus);

    }

    //Обновление при закреплении/откреплении заметки
    public void updateItemBind(ItemNote itemNote) {
        int index = listNoteCreate.indexOf(itemNote.getCreate());

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
            int index = listNoteCreate.indexOf(aNtCreate);

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
        int index = listNoteCreate.indexOf(itemNote.getCreate());

        if (index != -1) {
            ItemStatus itemStatus = listStatus.get(index);
            itemStatus.cancelNote();

            listNoteCreate.remove(index);
            listStatus.remove(index);
        }

    }

}
