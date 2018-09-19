package sgtmelon.scriptum.app.model.repo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;
import sgtmelon.scriptum.app.model.item.ItemNote;
import sgtmelon.scriptum.app.model.item.ItemRoll;
import sgtmelon.scriptum.app.model.item.ItemStatus;
import sgtmelon.scriptum.office.annot.def.db.DefCheck;
import sgtmelon.scriptum.office.annot.def.db.DefDb;

public class RepoNote implements Parcelable {

    private long id;
    private String text;

    protected RepoNote(Parcel in) {
        id = in.readLong();
        text = in.readString();
    }

    public static final Creator<RepoNote> CREATOR = new Creator<RepoNote>() {
        @Override
        public RepoNote createFromParcel(Parcel in) {
            return new RepoNote(in);
        }

        @Override
        public RepoNote[] newArray(int size) {
            return new RepoNote[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(text);
    }


    @Embedded
    private ItemNote itemNote;

    @Relation(parentColumn = DefDb.NT_ID, entityColumn = DefDb.RL_ID_NT)
    private List<ItemRoll> listRoll;

    @Ignore
    private ItemStatus itemStatus;

    public RepoNote(ItemNote itemNote, List<ItemRoll> listRoll, ItemStatus itemStatus) {
        this.itemNote = itemNote;
        this.listRoll = listRoll;
        this.itemStatus = itemStatus;

        id = itemNote.getId();
        text = Long.toString(itemNote.getId());
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
        itemStatus.updateNote(itemNote, true);
    }

    public void updateItemStatus(List<Long> rankVisible) {
        itemStatus.updateNote(itemNote, rankVisible);
    }

}
