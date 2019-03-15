package sgtmelon.scriptum.app.room.dao;

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
import sgtmelon.scriptum.app.model.RankModel;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.model.item.RankItem;
import sgtmelon.scriptum.app.model.key.NoteType;
import sgtmelon.scriptum.app.room.RoomDb;
import sgtmelon.scriptum.app.room.converter.NoteTypeConverter;

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
        final List<RankItem> rankList = getSimple();

        for (final RankItem rankItem : rankList) {
            final List<Long> noteIdList = rankItem.getNoteId();

            rankItem.setTextCount(
                    getNoteCount(new NoteTypeConverter().toInt(NoteType.TEXT), noteIdList)
            );

            rankItem.setRollCount(
                    getNoteCount(new NoteTypeConverter().toInt(NoteType.ROLL), noteIdList)
            );
        }

        return rankList;
    }

    @NonNull
    public RankModel get() {
        final List<RankItem> rankList = getComplex();

        final List<String> nameList = new ArrayList<>();
        for (final RankItem item : rankList) {
            nameList.add(item.getName().toUpperCase());
        }

        return new RankModel(rankList, nameList);
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
        final List<RankItem> rankList = getSimple();

        final boolean[] check = new boolean[rankList.size()];
        for (int i = 0; i < rankList.size(); i++) {
            final RankItem rankItem = rankList.get(i);
            check[i] = id.contains(rankItem.getId());
        }

        return check;
    }

    /**
     * Добавление или удаление id заметки к категорииё
     *
     * @param noteId     - Id заметки
     * @param rankIdList - Id категорий принадлежащих каметке
     */
    public void update(long noteId, List<Long> rankIdList) {
        final List<RankItem> rankList = getSimple();
        final boolean[] check = getCheck(rankIdList);

        for (int i = 0; i < rankList.size(); i++) {
            final RankItem rankItem = rankList.get(i);
            final List<Long> noteIdList = rankItem.getNoteId();

            if (check[i] && !noteIdList.contains(noteId)) {
                noteIdList.add(noteId);
            } else if (!check[i]) {
                noteIdList.remove(noteId);
            }
        }

        updateRank(rankList);
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

        final List<RankItem> rankList = getComplex();
        final List<Long> noteIdList = new ArrayList<>();

        for (int i = iStart; i <= iEnd; i++) {
            final RankItem rankItem = rankList.get(i);

            for (long id : rankItem.getNoteId()) {
                if (!noteIdList.contains(id)) {
                    noteIdList.add(id);
                }
            }

            final boolean start = i == dragFrom;
            final boolean end = i == dragTo;

            final int newPosition = start ? dragTo : i + iAdd;
            rankItem.setPosition(newPosition);

            if (startFirst) {
                if (end) {
                    rankList.remove(i);
                    rankList.add(newPosition, rankItem);
                } else {
                    rankList.set(i, rankItem);
                }
            } else {
                if (start) {
                    rankList.remove(i);
                    rankList.add(newPosition, rankItem);
                } else {
                    rankList.set(i, rankItem);
                }
            }
        }

        if (rankList.get(0).getPosition() != 0) {
            Collections.reverse(rankList);
        }

        updateRank(rankList);
        update(noteIdList, rankList);

        return rankList;
    }

    /**
     * @param position - Позиция удаления категории
     */
    public void update(int position) {
        final List<RankItem> rankList = getSimple();
        final List<Long> noteIdList = new ArrayList<>();

        for (int i = position; i < rankList.size(); i++) {
            final RankItem rankItem = rankList.get(i);

            for (final long id : rankItem.getNoteId()) {
                if (!noteIdList.contains(id)) {
                    noteIdList.add(id);
                }
            }

            rankItem.setPosition(i);
        }

        updateRank(rankList);
        update(noteIdList, rankList);
    }

    /**
     * @param noteIdList - Id заметок, которые нужно обновить
     * @param rankList   - Новый список категорий, с новыми позициями у категорий
     */
    private void update(List<Long> noteIdList, List<RankItem> rankList) {
        final List<NoteItem> noteList = getNote(noteIdList);

        for (final NoteItem noteItem : noteList) {
            final List<Long> oldIdList = noteItem.getRankId();

            final List<Long> newIdList = new ArrayList<>();
            final List<Long> newPsList = new ArrayList<>();

            for (final RankItem rankItem : rankList) {
                final long id = rankItem.getId();
                final long ps = rankItem.getPosition();

                if (oldIdList.contains(id)) {
                    newIdList.add(id);
                    newPsList.add(ps);
                }
            }

            noteItem.setRankId(newIdList);
            noteItem.setRankPs(newPsList);
        }

        updateNote(noteList);
    }

    @Delete
    abstract void delete(RankItem rankItem);

    public void delete(String name) {
        final RankItem rankItem = get(name);

        final List<Long> noteIdList = rankItem.getNoteId();
        if (noteIdList.size() != 0) {
            updateNote(noteIdList, rankItem.getId());
        }

        delete(rankItem);
    }

    public void listAll(TextView textView) {
        final List<RankItem> rankList = getSimple();

        final String annotation = "Rank Data Base: ";
        textView.setText(annotation);

        for (int i = 0; i < rankList.size(); i++) {
            final RankItem rankItem = rankList.get(i);

            textView.append("\n\n" +
                    "ID: " + rankItem.getId() + " | " +
                    "PS: " + rankItem.getPosition() + "\n" +
                    "NM: " + rankItem.getName() + "\n" +
                    "CR: " + TextUtils.join(", ", rankItem.getNoteId()) + "\n" +
                    "VS: " + rankItem.isVisible());
        }
    }

}