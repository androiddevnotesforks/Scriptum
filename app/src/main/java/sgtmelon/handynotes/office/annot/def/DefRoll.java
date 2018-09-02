package sgtmelon.handynotes.office.annot.def;

import androidx.annotation.IntDef;

@IntDef({DefRoll.read, DefRoll.write})
public @interface DefRoll {

    int read = 0, write = 1;

}
