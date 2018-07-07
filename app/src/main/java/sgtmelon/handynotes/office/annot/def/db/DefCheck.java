package sgtmelon.handynotes.office.annot.def.db;

import androidx.annotation.IntDef;

@IntDef({DefCheck.notDone, DefCheck.done})
public @interface DefCheck {

    String divider = "/";

    int notDone = 0, done = 1;  //Выполнение пункта

}
