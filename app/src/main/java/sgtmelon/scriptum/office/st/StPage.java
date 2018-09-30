package sgtmelon.scriptum.office.st;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IdRes;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.def.DefPage;

public class StPage implements Parcelable {

    public static final Creator<StPage> CREATOR = new Creator<StPage>() {
        @Override
        public StPage createFromParcel(Parcel in) {
            return new StPage(in);
        }

        @Override
        public StPage[] newArray(int size) {
            return new StPage[size];
        }
    };

    private int page;

    public StPage() {

    }

    private StPage(Parcel in) {
        page = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(page);
    }

    public int getPage() {
        return page;
    }

    public boolean setPage(@IdRes int itemId) {
        boolean scroll = false;
        switch (itemId) {
            case R.id.menu_actMain_pageRank:
                if (page == DefPage.rank) scroll = true;
                else page = DefPage.rank;
                break;
            case R.id.menu_actMain_pageNote:
                if (page == DefPage.notes) scroll = true;
                else page = DefPage.notes;
                break;
            case R.id.menu_actMain_pageBin:
                if (page == DefPage.bin) scroll = true;
                else page = DefPage.bin;
                break;
        }
        return scroll;
    }

}
