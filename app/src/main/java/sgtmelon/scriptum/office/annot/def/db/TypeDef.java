package sgtmelon.scriptum.office.annot.def.db;

import androidx.annotation.IntDef;

/**
 * Типы заметок
 */
@IntDef({
        TypeDef.text,
        TypeDef.roll
})
public @interface TypeDef {

    int text = 0, roll = 1;

}
