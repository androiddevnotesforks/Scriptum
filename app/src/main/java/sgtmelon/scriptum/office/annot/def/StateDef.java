package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

// TODO: 30.10.2018 перенести сюда BinDef и CheckDef

/**
 * Аннотация для описания каких-либо состояний
 */
public @interface StateDef {

    @IntDef({Bin.out, Bin.in})
    @interface Bin{
        int out = 0, in = 1;
    }

    @IntDef({Check.notDone, Check.done})
    @interface Check {
        String divider = "/";

        int notDone = 0, done = 1;
    }

}
