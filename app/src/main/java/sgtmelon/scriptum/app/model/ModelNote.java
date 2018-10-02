package sgtmelon.scriptum.app.model;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.app.model.item.ItemStatus;
import sgtmelon.scriptum.office.annot.def.db.DefCheck;
import sgtmelon.scriptum.office.annot.AnnDb;

public final class ModelNote { // TODO: 02.10.2018 чистая модель

    @Embedded
    private ItemNote itemNote;

    @Relation(parentColumn = AnnDb.NT_ID, entityColumn = AnnDb.RL_ID_NT)
    private List<ItemRoll> listRoll;

    @Ignore
    private ItemStatus itemStatus;

    public ModelNote(ItemNote itemNote, List<ItemRoll> listRoll, ItemStatus itemStatus) {
        this.itemNote = itemNote;
        this.listRoll = listRoll;
        this.itemStatus = itemStatus;
    }

    public ItemNote getItemNote() {
        return itemNote;
    }

    public void setItemNote(ItemNote itemNote) {
        this.itemNote = itemNote;
    }

    public List<ItemRoll> getListRoll() {
        return listRoll;
    }

    public void setListRoll(List<ItemRoll> listRoll) {
        this.listRoll = listRoll;
    }

    /**
     * Сброс списка при конвертированиия Список -> Текст
     */
    public void setListRoll() {
        listRoll = new ArrayList<>();
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    /**
     * При отметке всех пунктов
     */
    public void updateListRoll(@DefCheck int rollCheck) {
        for (int i = 0; i < listRoll.size(); i++) {
            ItemRoll itemRoll = listRoll.get(i);
            itemRoll.setCheck(rollCheck == DefCheck.done);
            listRoll.set(i, itemRoll);
        }
    }

    public void updateItemStatus(boolean noteStatus) {
        if (noteStatus) itemStatus.notifyNote();
        else itemStatus.cancelNote();
    }

    public void updateItemStatus() {
        itemStatus.updateNote(itemNote, true);
    }

    public void updateItemStatus(List<Long> rankVisible) {
        itemStatus.updateNote(itemNote, rankVisible);
    }

}
