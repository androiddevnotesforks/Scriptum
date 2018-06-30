package sgtmelon.handynotes.db.repo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.db.item.ItemNote;
import sgtmelon.handynotes.db.item.ItemRoll;
import sgtmelon.handynotes.db.item.ItemStatus;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.db.DefCheck;

public class RepoNote {

    @Embedded
    private ItemNote itemNote;

    @Relation(parentColumn = Db.NT_ID, entityColumn = Db.RL_ID_NT)
    private List<ItemRoll> listRoll;

    @Ignore
    private ItemStatus itemStatus;

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

    public ItemStatus getItemStatus() {
        return itemStatus;
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

}
