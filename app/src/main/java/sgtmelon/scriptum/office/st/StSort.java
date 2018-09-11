package sgtmelon.scriptum.office.st;

import java.util.List;

import sgtmelon.scriptum.app.model.item.ItemSort;
import sgtmelon.scriptum.office.annot.def.DefSort;

public class StSort {

    private int end;

    public int getEnd() {
        return end;
    }

    public void updateEnd(List<ItemSort> listSort) {
        for (int i = 0; i < listSort.size(); i++) {
            @DefSort int key = listSort.get(i).getKey();

            if (key == DefSort.create || key == DefSort.change) {
                end = i;
                break;
            }
        }
    }

}
