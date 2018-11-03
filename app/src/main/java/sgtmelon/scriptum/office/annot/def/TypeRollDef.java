package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

@IntDef({TypeRollDef.read, TypeRollDef.write})
public @interface TypeRollDef {

    int read = 0, write = 1;

}
