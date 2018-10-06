package sgtmelon.scriptum.app.database.dao;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import sgtmelon.scriptum.app.model.RankModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RankItem;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.conv.ListConv;

@Dao
public abstract class RankDao extends BaseDao {

    @Query("SELECT COUNT(RK_ID) FROM RANK_TABLE")
    public abstract long getCount();

    @Insert
    public abstract long insert(RankItem rankItem);

    @Query("SELECT * FROM RANK_TABLE " +
            "ORDER BY RK_POSITION ASC")
    abstract List<RankItem> getSimple();

    private List<RankItem> getComplex() {
        List<RankItem> listRank = getSimple();

        for (int i = 0; i < listRank.size(); i++) {
            RankItem rankItem = listRank.get(i);
            Long[] idNote = rankItem.getIdNote();

            rankItem.setTextCount(getNoteCount(TypeDef.text, idNote));
            rankItem.setRollCount(getNoteCount(TypeDef.roll, idNote));

            listRank.set(i, rankItem);
        }

        return listRank;
    }

    @Transaction
    public RankModel get() {
        List<RankItem> listRank = getComplex();
        List<String> listName = getNameUp();

        for (int i = 0; i < listName.size(); i++) {
            String name = listName.get(i);
            listName.set(i, name.toUpperCase());
        }

        return new RankModel(listRank, listName);
    }

    /**
     * @param name - Уникальное имя категории
     * @return - Модель категории
     */
    @Query("SELECT * FROM RANK_TABLE " +
            "WHERE RK_NAME = :name")
    abstract RankItem get(String name);

    /**
     * @return - Лист с именами в высоком регистре
     */
    @Query("SELECT UPPER(RK_NAME) FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract List<String> getNameUp();

    @Query("SELECT RK_NAME FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract String[] getName();

    @Query("SELECT RK_ID FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract Long[] getId();

    public boolean[] getCheck(Long[] id) {
        List<RankItem> listRank = getSimple();

        boolean[] check = new boolean[listRank.size()];
        for (int i = 0; i < listRank.size(); i++) {
            RankItem rankItem = listRank.get(i);
            check[i] = Arrays.asList(id).contains(rankItem.getId());
        }

        return check;
    }

    /**
     * Добавление или удаление id заметки к категорииё
     *
     * @param noteId - Id заметки
     * @param rankId - Id категорий принадлежащих каметке
     */
    public void update(long noteId, Long[] rankId) {
        List<RankItem> listRank = getSimple();
        boolean[] check = getCheck(rankId);

        for (int i = 0; i < listRank.size(); i++) {
            RankItem rankItem = listRank.get(i);

            List<Long> rkIdNt = ListConv.toList(rankItem.getIdNote());
            if (check[i]) {
                if (!rkIdNt.contains(noteId)) rkIdNt.add(noteId);
            } else {
                rkIdNt.remove(noteId);
            }

            rankItem.setIdNote(ListConv.fromList(rkIdNt));
            listRank.set(i, rankItem);
        }

        updateRank(listRank);
    }

    @Update
    public abstract void update(RankItem rankItem);

    /**
     * @param startDrag - Начальная позиция обновления
     * @param endDrag   - Конечная позиция обновления
     */
    public List<RankItem> update(int startDrag, int endDrag) {
        boolean startFirst = startDrag < endDrag;

        int iStart = startFirst ? startDrag : endDrag;
        int iEnd = startFirst ? endDrag : startDrag;
        int iAdd = startFirst ? -1 : 1;

        List<RankItem> listRank = getComplex();
        List<Long> idNote = new ArrayList<>();

        for (int i = iStart; i <= iEnd; i++) {
            RankItem rankItem = listRank.get(i);

            for (long id : rankItem.getIdNote()) {
                if (!idNote.contains(id)) {
                    idNote.add(id);
                }
            }

            boolean start = i == startDrag;
            boolean end = i == endDrag;

            int newPosition = start ? endDrag : i + iAdd;
            rankItem.setPosition(newPosition);

            if (startFirst) {
                if (end) {
                    listRank.remove(i);
                    listRank.add(newPosition, rankItem);
                } else listRank.set(i, rankItem);
            } else {
                if (start) {
                    listRank.remove(i);
                    listRank.add(newPosition, rankItem);
                } else listRank.set(i, rankItem);
            }
        }

        if (listRank.get(0).getPosition() != 0) {
            Collections.reverse(listRank);
        }

        updateRank(listRank);
        update(idNote, listRank);

        return listRank;
    }

    /**
     * @param position - Позиция удаления категории
     */
    public void update(int position) {
        List<RankItem> listRank = getSimple();
        List<Long> idNote = new ArrayList<>();

        for (int i = position; i < listRank.size(); i++) {
            RankItem rankItem = listRank.get(i);

            for (long id : rankItem.getIdNote()) {
                if (!idNote.contains(id)) {
                    idNote.add(id);
                }
            }

            rankItem.setPosition(i);
            listRank.set(i, rankItem);
        }

        updateRank(listRank);
        update(idNote, listRank);
    }

    /**
     * @param idNote   - Id заметок, которые нужно обновить
     * @param listRank - Новый список категорий, с новыми позициями у категорий
     */
    private void update(List<Long> idNote, List<RankItem> listRank) {
        List<NoteItem> listNote = getNote(idNote);

        for (int i = 0; i < listNote.size(); i++) {
            NoteItem noteItem = listNote.get(i);

            Long[] idOld = noteItem.getRankId();
            Long[] idNew = new Long[idOld.length];
            Long[] psNew = new Long[idOld.length];

            int p = 0;
            for (RankItem rankItem : listRank) {
                long id = rankItem.getId();
                long ps = rankItem.getPosition();

                if (Arrays.asList(idOld).contains(id)) {
                    idNew[p] = id;
                    psNew[p] = ps;
                    p++;
                }
            }

            noteItem.setRankId(idNew);
            noteItem.setRankPs(psNew);
            listNote.set(i, noteItem);
        }

        updateNote(listNote);
    }

    @Delete
    abstract void delete(RankItem rankItem);

    public void delete(String name) {
        RankItem rankItem = get(name);

        Long[] idNote = rankItem.getIdNote();
        if (idNote.length != 0) {
            updateNote(idNote, rankItem.getId());
        }

        delete(rankItem);
    }

    public void listAll(TextView textView) {
        List<RankItem> listRank = getSimple();

        String annotation = "Rank Data Base: ";
        textView.setText(annotation);

        for (int i = 0; i < listRank.size(); i++) {
            RankItem rankItem = listRank.get(i);

            textView.append("\n\n" +
                    "ID: " + rankItem.getId() + " | " +
                    "PS: " + rankItem.getPosition() + "\n" +
                    "NM: " + rankItem.getName() + "\n" +
                    "CR: " + TextUtils.join(", ", rankItem.getIdNote()) + "\n" +
                    "VS: " + rankItem.isVisible());
        }
    }

}
