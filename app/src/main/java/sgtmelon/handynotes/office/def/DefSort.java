package sgtmelon.handynotes.office.def;

import android.support.annotation.IntDef;

@IntDef({DefSort.create, DefSort.change, DefSort.rank, DefSort.color})
public @interface DefSort {

    String divider = ", ";

    int create = 0, change = 1;
    int rank = 2, color = 3;

}
