package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

@IntDef({TypeNoteDef.text, TypeNoteDef.roll})
public @interface TypeNoteDef {

    int text = 0, roll = 1;

}
