package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Аннотация для описания каких-либо состояний
 */
@IntDef({CheckDef.notDone, CheckDef.done})
public @interface CheckDef {

    String divider = "/";
    int notDone = 0, done = 1;

}