package sgtmelon.scriptum.office.st;

import java.util.List;

import androidx.annotation.NonNull;
import sgtmelon.scriptum.app.model.item.SortItem;
import sgtmelon.scriptum.office.annot.def.SortDef;

/**
 * Состояние для сортировки, определяющее позицию элемента {@link SortItem}, до которого будет происходить сортировка
 */
public final class SortSt {

    private int end;

    public int getEnd() {
        return end;
    }

    public void updateEnd(@NonNull List<SortItem> listSort) {
        for (int i = 0; i < listSort.size(); i++) {
            final int key = listSort.get(i).getKey();

            if (key == SortDef.create || key == SortDef.change) {
                end = i;
                break;
            }
        }
    }

}
