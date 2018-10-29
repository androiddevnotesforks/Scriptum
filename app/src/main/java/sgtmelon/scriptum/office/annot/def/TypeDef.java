package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Декларирование различных типов
 */
public @interface TypeDef {

    @IntDef({
            TypeDef.Note.text,
            TypeDef.Note.roll
    })
    @interface Note {
        int text = 0, roll = 1;
    }

    @IntDef({
            TypeDef.Roll.read,
            TypeDef.Roll.write
    })
    @interface Roll{
        int read = 0, write = 1;
    }

}
