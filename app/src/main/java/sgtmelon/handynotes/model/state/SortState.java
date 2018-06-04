package sgtmelon.handynotes.model.state;

import java.util.List;

import sgtmelon.handynotes.model.item.ItemSort;
import sgtmelon.handynotes.service.Help;

public class SortState {

    private int end;

    public int getEnd() {
        return end;
    }

    public void updateEnd(List<ItemSort> listSort) {
        for (int k = 0; k < listSort.size(); k++) {
            int key = listSort.get(k).getKey();
            if (key == Help.Pref.sortCr || key == Help.Pref.sortCh) {
                end = k;
                break;
            }
        }
    }

}
