package sgtmelon.handynotes.office.def.data;

import android.support.annotation.IntDef;

@IntDef({DefBin.out, DefBin.in})
public @interface DefBin {

    int out = 0, in = 1;    //Расположение относительно карзины

}
