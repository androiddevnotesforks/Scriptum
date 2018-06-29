package sgtmelon.handynotes.app.model.manager;

import java.util.List;

import sgtmelon.handynotes.office.annot.def.db.DefCheck;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.item.ItemRollView;

public class ManagerRoll {

    private final List<Long> listId;
    private final List<ItemRollView> listRollView;

    public ManagerRoll(List<Long> listId, List<ItemRollView> listRollView) {
        this.listId = listId;
        this.listRollView = listRollView;
    }

    public List<ItemRoll> getListRoll(long noteId) {
        int index = listId.indexOf(noteId);
        return listRollView.get(index).getListRoll();
    }

    public void insertList(long noteId, ItemRollView listRoll) {
        listId.add(noteId);
        listRollView.add(listRoll);
    }

    public void updateList(Long noteCreate, @DefCheck int rollCheck) {
        int index = listId.indexOf(noteCreate);

        ItemRollView itemRollView = listRollView.get(index);
        List<ItemRoll> listRoll = itemRollView.getListRoll();

        for (int i = 0; i < listRoll.size(); i++) {
            ItemRoll itemRoll = listRoll.get(i);
            itemRoll.setCheck(rollCheck == DefCheck.done);
            listRoll.set(i, itemRoll);
        }

        itemRollView.setListRoll(listRoll);
        listRollView.set(index, itemRollView);
    }

    public void removeList(String ntCreate) {
        int index = listId.indexOf(ntCreate);

        if (index != -1) {
            listId.remove(index);
            listRollView.remove(index);
        }
    }

    public void removeList(List<ItemNote> listNote) {
        for (ItemNote itemNote : listNote) {
            if (listId.contains(itemNote.getCreate())) {
                removeList(itemNote.getCreate());
            }
        }
    }

}
