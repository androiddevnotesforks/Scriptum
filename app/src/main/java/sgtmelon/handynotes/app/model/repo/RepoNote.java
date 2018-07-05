package sgtmelon.handynotes.app.model.repo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.item.ItemStatus;
import sgtmelon.handynotes.office.annotation.Db;
import sgtmelon.handynotes.office.annotation.def.db.DefCheck;

public class RepoNote {

    @Embedded
    private ItemNote itemNote;

    @Relation(parentColumn = Db.NT_ID, entityColumn = Db.RL_ID_NT)
    private List<ItemRoll> listRoll;

    @Ignore
    private ItemStatus itemStatus;

    public RepoNote() {

    }

    public RepoNote(ItemNote itemNote, List<ItemRoll> listRoll, ItemStatus itemStatus) {
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

    //Сброс списка при конвертировании
    public void setListRoll() {
        listRoll = new ArrayList<>();
    }

    public void setListRoll(List<ItemRoll> listRoll) {
        this.listRoll = listRoll;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    //При отметке всех пунктов
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
        itemStatus.updateNote(itemNote);
    }

    public void updateItemStatus(List<Long> rankVisible) {
        itemStatus.updateNote(itemNote, rankVisible);
    }
}
