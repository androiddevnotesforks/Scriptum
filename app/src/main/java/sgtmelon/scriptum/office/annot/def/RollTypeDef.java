package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Тип пункта списка
 */
@IntDef({
        RollTypeDef.read,
        RollTypeDef.write
})
public @interface RollTypeDef {

    int read = 0, write = 1;

}
