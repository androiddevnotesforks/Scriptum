package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Ключи сортировки данных
 */
@IntDef({
        SortDef.create,
        SortDef.change,
        SortDef.rank,
        SortDef.color
})
public @interface SortDef {

    String divider = ", ";

    int create = 0;
    int change = 1;
    int rank = 2;
    int color = 3;

    String def = create + divider + rank + divider + color;

}
