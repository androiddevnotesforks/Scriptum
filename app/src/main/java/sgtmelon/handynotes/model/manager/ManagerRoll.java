package sgtmelon.handynotes.model.manager;

import java.util.List;

import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.item.ItemRoll;
import sgtmelon.handynotes.model.item.ItemRollView;
import sgtmelon.handynotes.data.DataInfo;

public class ManagerRoll {

    private final List<String> listCreate;
    private final List<ItemRollView> listRollView;

    public ManagerRoll(List<String> listCreate, List<ItemRollView> listRollView) {
        this.listCreate = listCreate;
        this.listRollView = listRollView;
    }

    public List<ItemRoll> getListRoll(String ntCreate) {
        int index = listCreate.indexOf(ntCreate);
        return listRollView.get(index).getListRoll();
    }

    public void insertList(String ntCreate, ItemRollView listRoll) {
        listCreate.add(ntCreate);
        listRollView.add(listRoll);
    }

    public void updateList(String ntCreate, int rlCheck) {
        int index = listCreate.indexOf(ntCreate);

        ItemRollView itemRollView = listRollView.get(index);
        List<ItemRoll> listRoll = itemRollView.getListRoll();

        for (int i = 0; i < listRoll.size(); i++) {
            ItemRoll itemRoll = listRoll.get(i);
            itemRoll.setCheck(rlCheck == DataInfo.checkTrue);
            listRoll.set(i, itemRoll);
        }

        itemRollView.setListRoll(listRoll);
        listRollView.set(index, itemRollView);
    }

    public void removeList(String ntCreate) {
        int index = listCreate.indexOf(ntCreate);

        if (index != -1) {
            listCreate.remove(index);
            listRollView.remove(index);
        }
    }

    public void removeList(List<ItemNote> listNote) {
        for (ItemNote itemNote : listNote) {
            if (listCreate.contains(itemNote.getCreate())) {
                removeList(itemNote.getCreate());
            }
        }
    }

}
