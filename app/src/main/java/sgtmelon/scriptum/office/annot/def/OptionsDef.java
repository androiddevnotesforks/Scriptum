package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Дефолтные значения для привязки положения кнопки диалога опций к определённому методу
 */
public @interface OptionsDef {

    @IntDef({Text.bind, Text.convert, Text.copy, Text.delete})
    @interface Text {
        int bind = 0, convert = 1, copy = 2, delete = 3;
    }

    @IntDef({Roll.check, Roll.bind, Roll.convert, Roll.copy, Roll.delete})
    @interface Roll {
        int check = 0, bind = 1, convert = 2, copy = 3, delete = 4;

    }

    @IntDef({Bin.restore, Bin.copy, Bin.clear})
    @interface Bin {
        int restore = 0, copy = 1, clear = 2;
    }

}