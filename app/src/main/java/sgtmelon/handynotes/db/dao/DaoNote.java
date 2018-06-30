package sgtmelon.handynotes.db.dao;

import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.db.item.ItemNote;
import sgtmelon.handynotes.db.item.ItemStatus;
import sgtmelon.handynotes.db.repo.RepoNote;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.db.DefBin;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.conv.ConvBool;
import sgtmelon.handynotes.office.conv.ConvList;

@Dao
@TypeConverters({ConvBool.class})
public abstract class DaoNote extends DaoBase {

    @Insert
    public abstract long insert(ItemNote itemNote);

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_ID = :noteId")
    public abstract ItemNote get(long noteId);

    @Transaction
    @RawQuery
    abstract List<RepoNote> getQuery(SupportSQLiteQuery query);

    private List<RepoNote> getQuery(@DefBin int noteBin, String sortKeys) {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(
                "SELECT * FROM " + Db.NT_TB +
                        " WHERE " + Db.NT_BN + " = " + noteBin +
                        " ORDER BY " + sortKeys);

        return getQuery(query);
    }

    public List<RepoNote> get(Context context, @DefBin int noteBin, String sortKeys) {
        List<RepoNote> listRepoNote = getQuery(noteBin, sortKeys);
        List<Long> rankVisible = getRankVisible();
        Long[] rankVs = ConvList.fromList(rankVisible);

        for (int i = 0; i < listRepoNote.size(); i++) {
            RepoNote repoNote = listRepoNote.get(i);

            ItemNote itemNote = repoNote.getItemNote();
            ItemStatus itemStatus = new ItemStatus(context, itemNote, rankVs);

            Long[] rankId = itemNote.getRankId();
            if (rankId.length != 0 && !rankVisible.contains(rankId[0])) {
                itemStatus.cancelNote();
                listRepoNote.remove(i);
            } else {
                repoNote.setItemStatus(itemStatus);
                listRepoNote.set(i, repoNote);
            }
        }

        return listRepoNote;
    }

    @Query("SELECT * FROM NOTE_TABLE " +
            "WHERE NT_BIN = :noteBin")
    abstract List<ItemNote> get(@DefBin int noteBin);

    @Update
    public abstract void update(ItemNote itemNote);

    /**
     * Обновление положения заметки относительно корзины
     *
     * @param noteId     - Id обновляемой заметки
     * @param noteChange - Время изменения
     * @param noteBin    - Положение относительно корзины
     */
    @Query("UPDATE NOTE_TABLE " +
            "SET NT_CHANGE = :noteChange, NT_BIN = :noteBin " +
            "WHERE NT_ID = :noteId")
    public abstract void update(long noteId, String noteChange, boolean noteBin);

    /**
     * Обновление привязки к статус бару
     *
     * @param noteId     - Id обновляемой заметки
     * @param noteStatus - Привязка к статус бару
     */
    @Query("UPDATE NOTE_TABLE " +
            "SET NT_STATUS = :noteStatus " +
            "WHERE NT_ID = :noteId")
    public abstract void update(long noteId, boolean noteStatus);

    @Delete
    abstract void delete(ItemNote itemNote);

    @Delete
    abstract void delete(List<ItemNote> lisNote);

    public void delete(long noteId) {
        ItemNote itemNote = get(noteId);

        if (itemNote.getType() == DefType.roll) {
            deleteRoll(itemNote.getId());
        }

        Long[] rankId = itemNote.getRankId();
        if (rankId.length != 0) {
            clearRank(itemNote.getId(), rankId);
        }

        delete(itemNote);
    }

    public void clearBin() {
        List<ItemNote> listNote = get(DefBin.in);
        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);
            Long[] rankId = itemNote.getRankId();

            if (rankId.length != 0) {
                clearRank(itemNote.getId(), rankId);
            }
        }
        delete(listNote);
    }

    public void listAll(TextView textView) {
        List<RepoNote> listRepoNote = getQuery(DefBin.in, Db.orders[0]);
        listRepoNote.addAll(getQuery(DefBin.out, Db.orders[0]));

        String annotation = "Note Data Base:";
        textView.setText(annotation);

        for (int i = 0; i < listRepoNote.size(); i++) {
            ItemNote itemNote = listRepoNote.get(i).getItemNote();

            textView.append("\n\n" +
                    "ID: " + itemNote.getId() + " | " +
                    "CR: " + itemNote.getCreate() + " | " +
                    "CH: " + itemNote.getChange() + "\n");

            String noteName = itemNote.getName();
            if (!noteName.equals("")) textView.append("NM: " + noteName + "\n");

            String noteText = itemNote.getText();
            textView.append("TX: " + noteText.substring(0, Math.min(noteText.length(), 45)).replace("\n", " "));
            if (noteText.length() > 40) textView.append("...");
            textView.append("\n");

            textView.append("CL: " + itemNote.getColor() + " | " +
                    "TP: " + itemNote.getType() + " | " +
                    "BN: " + itemNote.isBin() + "\n" +
                    "RK ID: " + TextUtils.join(", ", itemNote.getRankId()) + " | " +
                    "RK PS: " + TextUtils.join(", ", itemNote.getRankPs()) + "\n" +
                    "ST: " + itemNote.isStatus());
        }
    }

}
