package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

@IntDef({BinDef.out, BinDef.in})
public @interface BinDef {
    int out = 0, in = 1;
}