package sgtmelon.scriptum.office.annot.def.db;

import androidx.annotation.IntDef;

@IntDef({
        DefBin.out,
        DefBin.in
})
public @interface DefBin {

    int out = 0, in = 1;    //Расположение относительно карзины

}
