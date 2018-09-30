package sgtmelon.scriptum.office.st;

import android.os.Parcel;
import android.os.Parcelable;

public class StNote implements Parcelable {

    public static final Creator<StNote> CREATOR = new Creator<StNote>() {
        @Override
        public StNote createFromParcel(Parcel in) {
            return new StNote(in);
        }

        @Override
        public StNote[] newArray(int size) {
            return new StNote[size];
        }
    };

    private boolean create;
    private boolean first = true, edit, bin;

    public StNote(boolean create, boolean bin) {
        this.create = create;
        this.bin = bin;

        edit = create;
    }

    private StNote(Parcel in) {
        create = in.readByte() != 0;
        first = in.readByte() != 0;
        edit = in.readByte() != 0;
        bin = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (create ? 1 : 0));
        parcel.writeByte((byte) (first ? 1 : 0));
        parcel.writeByte((byte) (edit ? 1 : 0));
        parcel.writeByte((byte) (bin ? 1 : 0));
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public boolean isBin() {
        return bin;
    }

    public void setBin(boolean bin) {
        this.bin = bin;
    }

}
