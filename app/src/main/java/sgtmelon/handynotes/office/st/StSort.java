package sgtmelon.handynotes.office.st;

import java.util.List;

import sgtmelon.handynotes.office.annot.def.DefSort;
import sgtmelon.handynotes.app.model.item.ItemSort;

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
