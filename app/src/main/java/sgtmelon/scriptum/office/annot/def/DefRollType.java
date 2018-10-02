package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Тип пункта списка
 */
@IntDef({
        DefRollType.read,
        DefRollType.write
})
public @interface DefRollType {

    int read = 0, write = 1;

}
