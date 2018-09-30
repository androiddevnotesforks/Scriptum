package sgtmelon.scriptum.office.annot.def.db;

import androidx.annotation.IntDef;

@IntDef({
        DefType.text,
        DefType.roll
})
public @interface DefType {

    int text = 0, roll = 1; //Типы заметок

}
