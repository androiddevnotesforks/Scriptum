package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;
import sgtmelon.scriptum.R;

/**
 * Ключи главного меню
 */
@IntDef({
        PageDef.rank,
        PageDef.notes,
        PageDef.bin
})
public @interface PageDef {

    int[] itemId = new int[]{
            R.id.item_page_rank,
            R.id.item_page_notes,
            R.id.item_page_bin,
    };

    int rank = 0, notes = 1, bin = 2;

}
