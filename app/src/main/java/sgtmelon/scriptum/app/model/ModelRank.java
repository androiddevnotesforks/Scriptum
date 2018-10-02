package sgtmelon.scriptum.app.model;

import java.util.List;

import sgtmelon.scriptum.app.model.item.ItemRank;

public final class ModelRank { // TODO: 02.10.2018 чистая модель

    private final List<String> listName;
    private List<ItemRank> listRank;

    public ModelRank(List<ItemRank> listRank, List<String> listName) {
        this.listRank = listRank;
        this.listName = listName;
    }

    public List<String> getListName() {
        return listName;
    }

    public List<ItemRank> getListRank() {
        return listRank;
    }

    public void setListRank(List<ItemRank> listRank) {
        this.listRank = listRank;
    }

    public int size() {
        return listRank.size();
    }

    public void add(int position, ItemRank itemRank) {
        listRank.add(position, itemRank);
        listName.add(position, itemRank.getName().toUpperCase());
    }

    public void set(int position, ItemRank itemRank) {
        listRank.set(position, itemRank);
        listName.set(position, itemRank.getName().toUpperCase());
    }

    public ItemRank get(int position) {
        return listRank.get(position);
    }

    public void remove(int position) {
        listRank.remove(position);
        listName.remove(position);
    }

    public void move(int oldPosition, int newPosition) {
        ItemRank itemRank = listRank.get(oldPosition);

        remove(oldPosition);
        add(newPosition, itemRank);
    }

}
