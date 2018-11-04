package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;
import sgtmelon.scriptum.app.control.InputControl;

/**
 * Аннотация для {@link InputControl}
 */
@IntDef({
        InputDef.name,
        InputDef.rank,
        InputDef.color,
        InputDef.text,
        InputDef.rollAdd,
        InputDef.roll,
        InputDef.rollSwipe,
        InputDef.rollMove
})
public @interface InputDef {

    int rank = 0;
    int color = 1;

    int name = 2;
    int text = 3;
    int roll = 4;

    int rollAdd = 5;
    int rollSwipe = 6;
    int rollMove = 7;

}
