package sgtmelon.handynotes.app.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRank;
import sgtmelon.handynotes.app.model.repo.RepoRank;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.conv.ConvList;

@Dao
public abstract class DaoRank extends DaoBase {

    @Query("SELECT COUNT(RK_ID) FROM RANK_TABLE")
    public abstract long getCount();

    @Insert
    public abstract long insert(ItemRank itemRank);

    @Query("SELECT * FROM RANK_TABLE " +
            "ORDER BY RK_POSITION ASC")
    abstract List<ItemRank> getSimple();

    private List<ItemRank> getComplex() {
        List<ItemRank> listRank = getSimple();

        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);
            Long[] idNote = itemRank.getIdNote();

            itemRank.setTextCount(getNoteCount(DefType.text, idNote));
            itemRank.setRollCount(getNoteCount(DefType.roll, idNote));
            itemRank.setRollCheck(getRollCheck(idNote), getRollCount(idNote));

            listRank.set(i, itemRank);
        }

        return listRank;
    }

    @Transaction
    public RepoRank get() {
        List<ItemRank> listRank = getComplex();
        List<String> listName = getNameUp();

        return new RepoRank(listRank, listName);
    }

    /**
     * @param name - Уникальное имя категории
     * @return - Модель категории
     */
    @Query("SELECT * FROM RANK_TABLE " +
            "WHERE RK_NAME = :name")
    abstract ItemRank get(String name);

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
        List<ItemRank> listRank = getSimple();

        boolean[] check = new boolean[listRank.size()];
        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);
            check[i] = Arrays.asList(id).contains(itemRank.getId());
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
        List<ItemRank> listRank = getSimple();
        boolean[] check = getCheck(rankId);

        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);

            List<Long> rkIdNt = ConvList.toList(itemRank.getIdNote());
            if (check[i]) {
                if (!rkIdNt.contains(noteId)) rkIdNt.add(noteId);
            } else {
                if (rkIdNt.contains(noteId)) rkIdNt.remove(noteId);
            }

            itemRank.setIdNote(ConvList.fromList(rkIdNt));
            listRank.set(i, itemRank);
        }

        updateRank(listRank);
    }

    @Update
    public abstract void update(ItemRank itemRank);

    /**
     * @param startDrag - Начальная позиция обновления
     * @param endDrag   - Конечная позиция обновления
     */
    public List<ItemRank> update(int startDrag, int endDrag) {
        boolean startFirst = startDrag < endDrag;

        int iStart = startFirst ? startDrag : endDrag;
        int iEnd = startFirst ? endDrag : startDrag;
        int iAdd = startFirst ? -1 : 1;

        List<ItemRank> listRank = getComplex();
        List<Long> idNote = new ArrayList<>();

        for (int i = iStart; i <= iEnd; i++) {
            ItemRank itemRank = listRank.get(i);

            for (long id : itemRank.getIdNote()) {
                if (!idNote.contains(id)) {
                    idNote.add(id);
                }
            }

            boolean start = i == startDrag;
            boolean end = i == endDrag;

            int newPosition = start ? endDrag : i + iAdd;
            itemRank.setPosition(newPosition);

            if (startFirst) {
                if (end) {
                    listRank.remove(i);
                    listRank.add(newPosition, itemRank);
                } else listRank.set(i, itemRank);
            } else {
                if (start) {
                    listRank.remove(i);
                    listRank.add(newPosition, itemRank);
                } else listRank.set(i, itemRank);
            }
        }

        if (listRank.get(0).getPosition() != 0) { // FIXME: 01.07.2018 Сделай нормально
            Collections.reverse(listRank);
        }

        updateRank(listRank);
        update(idNote, listRank);

//        for (ItemRank item : listRank) {
//            Log.i("DaoRank", "NM: " + item.getName() + " | PS: " + item.getPosition());
//        }

        return listRank;
    }

    /**
     * @param position - Позиция удаления категории
     */
    public void update(int position) {
        List<ItemRank> listRank = getSimple();
        List<Long> idNote = new ArrayList<>();

        for (int i = position; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);

            for (long id : itemRank.getIdNote()) {
                if (!idNote.contains(id)) {
                    idNote.add(id);
                }
            }

            itemRank.setPosition(i);
            listRank.set(i, itemRank);
        }

        updateRank(listRank);
        update(idNote, listRank);
    }

    /**
     * @param idNote   - Id заметок, которые нужно обновить
     * @param listRank - Новый список категорий, с новыми позициями у категорий
     */
    private void update(List<Long> idNote, List<ItemRank> listRank) {
        List<ItemNote> listNote = getNote(idNote);

        for (int i = 0; i < listNote.size(); i++) {
            ItemNote itemNote = listNote.get(i);

            Long[] idOld = itemNote.getRankId();
            Long[] idNew = new Long[idOld.length];
            Long[] psNew = new Long[idOld.length];

            int p = 0;
            for (ItemRank itemRank : listRank) {
                long id = itemRank.getId();
                long ps = itemRank.getPosition();

                if (Arrays.asList(idOld).contains(id)) {
                    idNew[p] = id;
                    psNew[p] = ps;
                    p++;
                }
            }

            itemNote.setRankId(idNew);
            itemNote.setRankPs(psNew);
            listNote.set(i, itemNote);
        }

        updateNote(listNote);
    }

    @Delete
    abstract void delete(ItemRank itemRank);

    public void delete(String name) {
        ItemRank itemRank = get(name);

        Long[] idNote = itemRank.getIdNote();
        if (idNote.length != 0) {
            updateNote(idNote, itemRank.getId());
        }

        delete(itemRank);
    }

    public void listAll(TextView textView) {
        List<ItemRank> listRank = getSimple();

        String annotation = "Rank Data Base: ";
        textView.setText(annotation);

        for (int i = 0; i < listRank.size(); i++) {
            ItemRank itemRank = listRank.get(i);

            textView.append("\n\n" +
                    "ID: " + itemRank.getId() + " | " +
                    "PS: " + itemRank.getPosition() + "\n" +
                    "NM: " + itemRank.getName() + "\n" +
                    "CR: " + TextUtils.join(", ", itemRank.getIdNote()) + "\n" +
                    "VS: " + itemRank.isVisible());
        }
    }
}
