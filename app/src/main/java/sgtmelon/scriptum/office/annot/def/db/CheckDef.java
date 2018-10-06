package sgtmelon.scriptum.office.annot.def.db;

import androidx.annotation.IntDef;

/**
 * Выполнение пункта
 */
@IntDef({
        CheckDef.notDone,
        CheckDef.done
})
public @interface CheckDef {

    int notDone = 0, done = 1;

    String divider = "/";

}
