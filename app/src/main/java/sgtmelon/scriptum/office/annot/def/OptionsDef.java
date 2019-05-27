package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Дефолтные значения для привязки положения кнопки диалога опций к определённому методу
 */
public @interface OptionsDef {

    @IntDef({Note.bind, Note.convert, Note.copy, Note.delete})
    @interface Note {
        int bind = 0, convert = 1, copy = 2, delete = 3;
    }

    @IntDef({Bin.restore, Bin.copy, Bin.clear})
    @interface Bin {
        int restore = 0, copy = 1, clear = 2;
    }

}