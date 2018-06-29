package sgtmelon.handynotes.office.annot.def.data;

import android.support.annotation.IntDef;

@IntDef({DefCheck.notDone, DefCheck.done})
public @interface DefCheck {

    String divider = "/";

    int notDone = 0, done = 1;  //Выполнение пункта

}
