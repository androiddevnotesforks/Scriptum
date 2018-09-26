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
