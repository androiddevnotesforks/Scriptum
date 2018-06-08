package sgtmelon.handynotes.database.dao;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sgtmelon.handynotes.model.item.ItemRank;

public abstract class DaoRank {

    @Query("SELECT COUNT(*) FROM RANK_TABLE")
    public abstract long getRankCount();

    @Insert
    public abstract long insertRank(ItemRank itemRank);

    @Query("SELECT * FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract List<ItemRank> getRank();

    public List<String> getRankName() {
        List<ItemRank> listRank = getRank();

        List<String> listRankName = new ArrayList<>();
        for (int i = 0; i < listRank.size(); i++) {
            listRankName.add(listRank.get(i).getName().toUpperCase());
        }

        return listRankName;
    }

    public boolean[] getRankCheck(String[] rkId) {
        List<ItemRank> listRank = getRank();

        boolean[] rankCh = new boolean[listRank.size()];
        for (int i = 0; i < listRank.size(); i++) {
            String id = Integer.toString(listRank.get(i).getId());
            rankCh[i] = Arrays.asList(rkId).contains(id);
        }

        return rankCh;
    }

}
