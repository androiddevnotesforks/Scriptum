package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Ключи сортировки данных
 */
@IntDef({
        DefSort.create,
        DefSort.change,
        DefSort.rank,
        DefSort.color
})
public @interface DefSort {

    String divider = ", ";

    int create = 0;
    int change = 1;
    int rank = 2;
    int color = 3;

    String def = create + divider + rank + divider + color;

}
