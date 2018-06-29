package sgtmelon.handynotes.app.model.state;

import java.util.List;

import sgtmelon.handynotes.office.annot.def.DefSort;
import sgtmelon.handynotes.app.model.item.ItemSort;

public class StateSort {

    private int end;

    public int getEnd() {
        return end;
    }

    public void updateEnd(List<ItemSort> listSort) {
        for (int k = 0; k < listSort.size(); k++) {
            @DefSort int key = listSort.get(k).getKey();
            if (key == DefSort.create || key == DefSort.change) {
                end = k;
                break;
            }
        }
    }

}
