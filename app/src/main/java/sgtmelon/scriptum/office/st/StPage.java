package sgtmelon.scriptum.office.st;

import androidx.annotation.IdRes;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.annot.def.DefPage;

public class StPage {

    private int page;

    public int getPage() {
        return page;
    }

    public boolean setPage(@IdRes int itemId) {
        boolean add = false;
        switch (itemId) {
            case R.id.menu_actMain_pageRank:
                page = DefPage.rank;
                break;
            case R.id.menu_actMain_pageNote:
                if (page == DefPage.notes) add = true;
                else page = DefPage.notes;
                break;
            case R.id.menu_actMain_pageBin:
                page = DefPage.bin;
                break;
        }
        return add;
    }

}
