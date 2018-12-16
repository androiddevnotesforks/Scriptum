package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;
import sgtmelon.scriptum.app.control.InputControl;
import sgtmelon.scriptum.app.model.item.InputItem;

/**
 * Аннотация для {@link InputItem#tag}, {@link InputControl}
 */
@IntDef({
        InputDef.rank,
        InputDef.color,

        InputDef.name,
        InputDef.text,
        InputDef.roll,

        InputDef.rollAdd,
        InputDef.rollRemove,
        InputDef.rollMove
})
public @interface InputDef {

    int rank = 0;
    int color = 1;

    int name = 2;
    int text = 3;
    int roll = 4;

    int rollAdd = 5;
    int rollRemove = 6;
    int rollMove = 7;

}