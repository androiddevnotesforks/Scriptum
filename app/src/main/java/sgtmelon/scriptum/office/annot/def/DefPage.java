package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;
import sgtmelon.scriptum.R;

@IntDef({
        DefPage.rank,
        DefPage.notes,
        DefPage.bin
})
public @interface DefPage {

    int[] itemId = new int[]{
            R.id.menu_actMain_pageRank,
            R.id.menu_actMain_pageNote,
            R.id.menu_actMain_pageBin,
    };

    int rank = 0;
    int notes = 1;
    int bin = 2;

}
