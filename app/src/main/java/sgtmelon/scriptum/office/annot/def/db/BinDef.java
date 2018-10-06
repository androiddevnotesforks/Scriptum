package sgtmelon.scriptum.office.annot.def.db;

import androidx.annotation.IntDef;

/**
 * Расположение относительно карзины
 */
@IntDef({
        BinDef.out,
        BinDef.in
})
public @interface BinDef {

    int out = 0, in = 1;

}
