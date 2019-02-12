package sgtmelon.scriptum.app.database.dao;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.model.RankRepo;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RankItem;
import sgtmelon.scriptum.office.annot.def.NoteType;
import sgtmelon.scriptum.office.conv.NoteTypeConv;

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

        for (RankItem rankItem : listRank) {
            final List<Long> listNoteId = rankItem.getNoteId();

            rankItem.setTextCount(getNoteCount(new NoteTypeConv().toInt(NoteType.TEXT), listNoteId));
            rankItem.setRollCount(getNoteCount(new NoteTypeConv().toInt(NoteType.ROLL), listNoteId));
        }

        return listRank;
    }

    @NonNull
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

    @Query("SELECT RK_NAME FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract String[] getName();

    @Query("SELECT RK_ID FROM RANK_TABLE " +
            "ORDER BY RK_POSITION")
    public abstract Long[] getId();

    public boolean[] getCheck(List<Long> id) {
        final List<RankItem> listRank = getSimple();

        final boolean[] check = new boolean[listRank.size()];
        for (int i = 0; i < listRank.size(); i++) {
            final RankItem rankItem = listRank.get(i);
            check[i] = id.contains(rankItem.getId());
        }

        return check;
    }

    /**
     * Добавление или удаление id заметки к категорииё
     *
     * @param noteId     - Id заметки
     * @param listRankId - Id категорий принадлежащих каметке
     */
    public void update(long noteId, List<Long> listRankId) {
        final List<RankItem> listRank = getSimple();
        final boolean[] check = getCheck(listRankId);

        for (int i = 0; i < listRank.size(); i++) {
            final RankItem rankItem = listRank.get(i);
            final List<Long> listId = rankItem.getNoteId();

            if (check[i] && !listId.contains(noteId)) {
                listId.add(noteId);
            } else if (!check[i]) {
                listId.remove(noteId);
            }
        }

        updateRank(listRank);
    }

    @Update
    public abstract void update(RankItem rankItem);

    /**
     * @param dragFrom - Начальная позиция обновления
     * @param dragTo   - Конечная позиция обновления
     */
    public List<RankItem> update(int dragFrom, int dragTo) {
        final boolean startFirst = dragFrom < dragTo;

        final int iStart = startFirst ? dragFrom : dragTo;
        final int iEnd = startFirst ? dragTo : dragFrom;
        final int iAdd = startFirst ? -1 : 1;

        final List<RankItem> listRank = getComplex();
        final List<Long> listNoteId = new ArrayList<>();

        for (int i = iStart; i <= iEnd; i++) {
            final RankItem rankItem = listRank.get(i);

            for (long id : rankItem.getNoteId()) {
                if (!listNoteId.contains(id)) {
                    listNoteId.add(id);
                }
            }

            final boolean start = i == dragFrom;
            final boolean end = i == dragTo;

            final int newPosition = start ? dragTo : i + iAdd;
            rankItem.setPosition(newPosition);

            if (startFirst) {
                if (end) {
                    listRank.remove(i);
                    listRank.add(newPosition, rankItem);
                } else {
                    listRank.set(i, rankItem);
                }
            } else {
                if (start) {
                    listRank.remove(i);
                    listRank.add(newPosition, rankItem);
                } else {
                    listRank.set(i, rankItem);
                }
            }
        }

        if (listRank.get(0).getPosition() != 0) {
            Collections.reverse(listRank);
        }

        updateRank(listRank);
        update(listNoteId, listRank);

        return listRank;
    }

    /**
     * @param position - Позиция удаления категории
     */
    public void update(int position) {
        final List<RankItem> listRank = getSimple();
        final List<Long> listNoteId = new ArrayList<>();

        for (int i = position; i < listRank.size(); i++) {
            final RankItem rankItem = listRank.get(i);

            for (long id : rankItem.getNoteId()) {
                if (!listNoteId.contains(id)) {
                    listNoteId.add(id);
                }
            }

            rankItem.setPosition(i);
        }

        updateRank(listRank);
        update(listNoteId, listRank);
    }

    /**
     * @param listNoteId - Id заметок, которые нужно обновить
     * @param listRank   - Новый список категорий, с новыми позициями у категорий
     */
    private void update(List<Long> listNoteId, List<RankItem> listRank) {
        final List<NoteItem> listNote = getNote(listNoteId);

        for (NoteItem noteItem : listNote) {
            final List<Long> idOld = noteItem.getRankId();

            final List<Long> idNew = new ArrayList<>();
            final List<Long> psNew = new ArrayList<>();

            for (RankItem rankItem : listRank) {
                final long id = rankItem.getId();
                final long ps = rankItem.getPosition();

                if (idOld.contains(id)) {
                    idNew.add(id);
                    psNew.add(ps);
                }
            }

            noteItem.setRankId(idNew);
            noteItem.setRankPs(psNew);
        }

        updateNote(listNote);
    }

    @Delete
    abstract void delete(RankItem rankItem);

    public void delete(String name) {
        final RankItem rankItem = get(name);

        final List<Long> listNoteId = rankItem.getNoteId();
        if (listNoteId.size() != 0) {
            updateNote(listNoteId, rankItem.getId());
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
                    "CR: " + TextUtils.join(", ", rankItem.getNoteId()) + "\n" +
                    "VS: " + rankItem.isVisible());
        }
    }

}