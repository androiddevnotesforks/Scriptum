package sgtmelon.handynotes.office.annot.def.db;

import android.support.annotation.IntDef;

@IntDef({DefBin.out, DefBin.in})
public @interface DefBin {

    int out = 0, in = 1;    //Расположение относительно карзины

}
