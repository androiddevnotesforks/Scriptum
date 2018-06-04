package sgtmelon.handynotes.model.item;

import java.util.List;

public class ItemRollView {

    public ItemRollView() {

    }

    public ItemRollView(List<ItemRoll> listRoll) {
        this.listRoll = listRoll;
    }

    private List<ItemRoll> listRoll;
    private int size;

    public List<ItemRoll> getListRoll() {
        return listRoll;
    }

    public void setListRoll(List<ItemRoll> listRoll) {
        this.listRoll = listRoll;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
