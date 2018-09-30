package sgtmelon.scriptum.office.st;

import android.os.Parcel;
import android.os.Parcelable;

public class StOpen implements Parcelable {

    public static final Creator<StOpen> CREATOR = new Creator<StOpen>() {
        @Override
        public StOpen createFromParcel(Parcel in) {
            return new StOpen(in);
        }

        @Override
        public StOpen[] newArray(int size) {
            return new StOpen[size];
        }
    };

    private boolean open = false;

    public StOpen() {

    }

    private StOpen(Parcel in) {
        open = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (open ? 1 : 0));
    }

    public boolean isNotOpen() {
        return !open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}
