package sgtmelon.handynotes.office.annotation.def;

import android.support.annotation.IntDef;

import sgtmelon.handynotes.R;

@IntDef({DefPages.rank, DefPages.notes, DefPages.bin})
public @interface DefPages {

    String PAGE = "CURRENT_PAGE";
    String CREATE = "IS_CREATE";

    int[] itemId = new int[]{
            R.id.menu_actMain_pageRank,
            R.id.menu_actMain_pageNote,
            R.id.menu_actMain_pageBin,
    };

    int rank = 0;
    int notes = 1;
    int bin = 2;

}
