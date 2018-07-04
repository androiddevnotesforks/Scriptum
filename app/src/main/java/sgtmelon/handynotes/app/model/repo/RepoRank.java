package sgtmelon.handynotes.app.model.repo;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.app.model.item.ItemRank;
import sgtmelon.handynotes.office.conv.ConvList;

public class RepoRank {

    private List<ItemRank> listRank;
    private List<String> listName;

    public RepoRank(List<ItemRank> listRank, List<String> listName) {
        this.listRank = listRank;
        this.listName = listName;
    }

    public List<ItemRank> getListRank() {
        return listRank;
    }

    public void setListRank(List<ItemRank> listRank) {
        this.listRank = listRank;
    }

    public List<String> getListName() {
        return listName;
    }

    public Long[] getVisible() {
        List<Long> rankVisible = new ArrayList<>();
        for (ItemRank itemRank : listRank) {
            if (itemRank.isVisible()) {
                rankVisible.add(itemRank.getId());
            }
        }
        return ConvList.fromList(rankVisible);
    }

    public int size() {
        return listRank.size();
    }

    public void add(int position, ItemRank itemRank) {
        listRank.add(position, itemRank);
        listName.add(position, itemRank.getName().toUpperCase());
    }

    public void remove(int position) {
        listRank.remove(position);
        listName.remove(position);
    }

    public ItemRank get(int position) {
        return listRank.get(position);
    }

    public void set(int position, ItemRank itemRank) {
        listRank.set(position, itemRank);
        listName.set(position, itemRank.getName().toUpperCase());
    }

    public void move(int oldPosition, int newPosition) {
        ItemRank itemRank = listRank.get(oldPosition);

        remove(oldPosition);
        add(newPosition, itemRank);
    }

}
