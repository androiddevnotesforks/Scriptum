package sgtmelon.handynotes.office.st;

import androidx.annotation.IdRes;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.def.DefPages;

public class StPage {

    private int page;

    public int getPage() {
        return page;
    }

    public boolean setPage(@IdRes int itemId) {
        boolean add = false;
        switch (itemId) {
            case R.id.menu_actMain_pageRank:
                page = DefPages.rank;
                break;
            case R.id.menu_actMain_pageNote:
                if (page == DefPages.notes) add = true;
                else page = DefPages.notes;
                break;
            case R.id.menu_actMain_pageBin:
                page = DefPages.bin;
                break;
        }
        return add;
    }

}
