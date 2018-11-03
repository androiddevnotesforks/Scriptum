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
import androidx.room.Update;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.RankRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RankItem;
import sgtmelon.scriptum.office.annot.def.TypeNoteDef;
import sgtmelon.scriptum.office.conv.ListConv;

/**
 * Класс для общения Dao категорий {@link RoomDb}
 */
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
        final List<RankItem> listRank = getSimple();

        for (int i = 0; i < listRank.size(); i++) {
            final RankItem rankItem = listRank.get(i);
            final Long[] idNote = rankItem.getIdNote();

            rankItem.setTextCount(getNoteCount(TypeNoteDef.text, idNote));
            rankItem.setRollCount(getNoteCount(TypeNoteDef.roll, idNote));

            listRank.set(i, rankItem);
        }

        return listRank;
    }

    public RankRepo get() {
        final List<RankItem> listRank = getComplex();

        final List<String> listName = new ArrayList<>();
        for (RankItem item : listRank) {
            listName.add(item.getName().toUpperCase());
        }

        return new RankRepo(listRank, listName);
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
        final List<RankItem> listRank = getSimple();

        final boolean[] check = new boolean[listRank.size()];
        for (int i = 0; i < listRank.size(); i++) {
            final RankItem rankItem = listRank.get(i);
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
        final List<RankItem> listRank = getSimple();
        final boolean[] check = getCheck(rankId);

        for (int i = 0; i < listRank.size(); i++) {
            final RankItem rankItem = listRank.get(i);

            final List<Long> rkIdNt = ListConv.toList(rankItem.getIdNote());
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
        final boolean startFirst = startDrag < endDrag;

        final int iStart = startFirst ? startDrag : endDrag;
        final int iEnd = startFirst ? endDrag : startDrag;
        final int iAdd = startFirst ? -1 : 1;

        final List<RankItem> listRank = getComplex();
        final List<Long> idNote = new ArrayList<>();

        for (int i = iStart; i <= iEnd; i++) {
            final RankItem rankItem = listRank.get(i);

            for (long id : rankItem.getIdNote()) {
                if (!idNote.contains(id)) {
                    idNote.add(id);
                }
            }

            final boolean start = i == startDrag;
            final boolean end = i == endDrag;

            final int newPosition = start ? endDrag : i + iAdd;
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
        final List<RankItem> listRank = getSimple();
        final List<Long> idNote = new ArrayList<>();

        for (int i = position; i < listRank.size(); i++) {
            final RankItem rankItem = listRank.get(i);

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
        final List<NoteItem> listNote = getNote(idNote);

        for (int i = 0; i < listNote.size(); i++) {
            final NoteItem noteItem = listNote.get(i);

            final Long[] idOld = noteItem.getRankId();
            final Long[] idNew = new Long[idOld.length];
            final Long[] psNew = new Long[idOld.length];

            int p = 0;
            for (RankItem rankItem : listRank) {
                final long id = rankItem.getId();
                final long ps = rankItem.getPosition();

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
        final RankItem rankItem = get(name);

        final Long[] idNote = rankItem.getIdNote();
        if (idNote.length != 0) {
            updateNote(idNote, rankItem.getId());
        }

        delete(rankItem);
    }

    public void listAll(TextView textView) {
        final List<RankItem> listRank = getSimple();

        final String annotation = "Rank Data Base: ";
        textView.setText(annotation);

        for (int i = 0; i < listRank.size(); i++) {
            final RankItem rankItem = listRank.get(i);

            textView.append("\n\n" +
                    "ID: " + rankItem.getId() + " | " +
                    "PS: " + rankItem.getPosition() + "\n" +
                    "NM: " + rankItem.getName() + "\n" +
                    "CR: " + TextUtils.join(", ", rankItem.getIdNote()) + "\n" +
                    "VS: " + rankItem.isVisible());
        }
    }

}
