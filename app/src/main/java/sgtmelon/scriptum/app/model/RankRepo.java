package sgtmelon.scriptum.app.model;

import java.util.List;

import sgtmelon.scriptum.app.model.item.RankItem;

/**
 * Репозиторий категории
 */
public final class RankRepo {

    private final List<String> listName;
    private List<RankItem> listRank;

    public RankRepo(List<RankItem> listRank, List<String> listName) {
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

    public void remove(int position) {
        listRank.remove(position);
        listName.remove(position);
    }

    public void move(int positionOld, int positionNew) {
        final RankItem rankItem = listRank.get(positionOld);

        remove(positionOld);
        add(positionNew, rankItem);
    }

}