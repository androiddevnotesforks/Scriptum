package sgtmelon.scriptum.office.annot.def.db;

import androidx.annotation.IntDef;

/**
 * Выполнение пункта
 */
@IntDef({
        DefCheck.notDone,
        DefCheck.done
})
public @interface DefCheck {

    int notDone = 0, done = 1;

    String divider = "/";

}
