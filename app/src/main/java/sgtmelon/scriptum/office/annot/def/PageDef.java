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
            R.id.page_rank_item,
            R.id.page_notes_item,
            R.id.page_bin_item,
    };

    int rank = 0, notes = 1, bin = 2;

}
