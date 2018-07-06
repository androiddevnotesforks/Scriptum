package sgtmelon.handynotes.office.annot.def.db;

import android.support.annotation.IntDef;

@IntDef({DefType.text, DefType.roll})
public @interface DefType {

    int text = 0, roll = 1; //Типы заметок

}
