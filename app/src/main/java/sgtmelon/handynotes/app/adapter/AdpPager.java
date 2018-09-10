package sgtmelon.handynotes.app.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import sgtmelon.handynotes.app.view.frg.FrgIntro;

public class AdpPager extends FragmentPagerAdapter {

    private final List<FrgIntro> itemList = new ArrayList<>();

    public AdpPager(FragmentManager fm) {
        super(fm);
    }

    public void addItem(FrgIntro frgIntro) {
        itemList.add(frgIntro);
    }

    @Override
    public FrgIntro getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

}
