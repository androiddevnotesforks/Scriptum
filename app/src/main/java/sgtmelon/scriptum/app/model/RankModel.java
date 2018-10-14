package sgtmelon.scriptum.app.model;

import java.util.List;

import sgtmelon.scriptum.app.model.item.RankItem;

public final class RankModel {

    private final List<String> listName;
    private List<RankItem> listRank;

    public RankModel(List<RankItem> listRank, List<String> listName) {
        this.listRank = listRank;
        this.listName = listName;
    }

    public List<String> getListName() {
        return listName;
    }

    public List<RankItem> getListRank() {
        return listRank;
    }

    public void setListRank(List<RankItem> listRank) {
        this.listRank = listRank;
    }

    public int size() {
        return listRank.size();
    }

    public void add(int position, RankItem rankItem) {
        listRank.add(position, rankItem);
        listName.add(position, rankItem.getName().toUpperCase());
    }

    public void set(int position, RankItem rankItem) {
        listRank.set(position, rankItem);
        listName.set(position, rankItem.getName().toUpperCase());
    }

    public RankItem get(int position) {
        return listRank.get(position);
    }

    public void remove(int position) {
        listRank.remove(position);
        listName.remove(position);
    }

    public void move(int oldPosition, int newPosition) {
        RankItem rankItem = listRank.get(oldPosition);

        remove(oldPosition);
        add(newPosition, rankItem);
    }

}
