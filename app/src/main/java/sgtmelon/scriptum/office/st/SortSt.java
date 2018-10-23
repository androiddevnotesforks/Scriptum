package sgtmelon.scriptum.office.st;

import java.util.List;

import sgtmelon.scriptum.app.model.item.SortItem;
import sgtmelon.scriptum.office.annot.def.SortDef;

public final class SortSt {

    private int end;

    public int getEnd() {
        return end;
    }

    public void updateEnd(List<SortItem> listSort) {
        for (int i = 0; i < listSort.size(); i++) {
            @SortDef int key = listSort.get(i).getKey();

            if (key == SortDef.create || key == SortDef.change) {
                end = i;
                break;
            }
        }
    }

}