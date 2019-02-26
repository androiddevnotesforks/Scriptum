package sgtmelon.scriptum.app.control.input;

import androidx.annotation.IntDef;
import sgtmelon.scriptum.app.model.item.InputItem;

/**
 * Аннотация для {@link InputItem#tag}, {@link InputControl}
 */
@IntDef({
        InputDef.indefinite,

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

    int indefinite = -1; //Не использовать

    int rank = 0;
    int color = 1;

    int name = 2;
    int text = 3;
    int roll = 4;

    int rollAdd = 5;
    int rollRemove = 6;
    int rollMove = 7;

}