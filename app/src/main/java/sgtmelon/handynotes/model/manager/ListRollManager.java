package sgtmelon.handynotes.model.manager;

import java.util.List;

import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemRoll;
import sgtmelon.handynotes.service.NoteDB;
import sgtmelon.handynotes.model.item.ItemRollView;

public class ListRollManager {

    private List<String> listNoteCreate;
    private List<ItemRollView> listRollView;

    public ListRollManager(List<String> listNoteCreate, List<ItemRollView> listRollView) {
        this.listNoteCreate = listNoteCreate;
        this.listRollView = listRollView;
    }

    public List<ItemRoll> getListRoll(String ntCreate) {
        int index = listNoteCreate.indexOf(ntCreate);
        return listRollView.get(index).getListRoll();
    }

    public void insertList(String ntCreate, ItemRollView listRoll) {
        listNoteCreate.add(ntCreate);
        listRollView.add(listRoll);
    }

    public void updateList(String ntCreate, int rlCheck) {
        int index = listNoteCreate.indexOf(ntCreate);

        ItemRollView itemRollView = listRollView.get(index);
        List<ItemRoll> listRoll = itemRollView.getListRoll();

        for (int i = 0; i < listRoll.size(); i++) {
            ItemRoll itemRoll = listRoll.get(i);
            itemRoll.setCheck(rlCheck == NoteDB.checkTrue);
            listRoll.set(i, itemRoll);
        }

        itemRollView.setListRoll(listRoll);
        listRollView.set(index, itemRollView);
    }

    public void removeList(String ntCreate) {
        int index = listNoteCreate.indexOf(ntCreate);

        if (index != -1) {
            listNoteCreate.remove(index);
            listRollView.remove(index);
        }
    }

    public void removeList(List<ItemNote> listNote) {
        for (ItemNote itemNote : listNote) {
            if (listNoteCreate.contains(itemNote.getCreate())) {
                removeList(itemNote.getCreate());
            }
        }
    }

}
