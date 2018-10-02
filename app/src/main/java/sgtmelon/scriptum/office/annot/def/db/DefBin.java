package sgtmelon.scriptum.office.annot.def.db;

import androidx.annotation.IntDef;

/**
 * Расположение относительно карзины
 */
@IntDef({
        DefBin.out,
        DefBin.in
})
public @interface DefBin {

    int out = 0, in = 1;

}
