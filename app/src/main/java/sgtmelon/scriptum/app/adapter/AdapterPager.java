package sgtmelon.scriptum.app.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import sgtmelon.scriptum.app.view.fragment.IntroFragment;

public final class AdapterPager extends FragmentPagerAdapter {

    private final List<IntroFragment> itemList = new ArrayList<>();

    public AdapterPager(FragmentManager fm) {
        super(fm);
    }

    public void addItem(IntroFragment introFragment) {
        itemList.add(introFragment);
    }

    @Override
    public IntroFragment getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

}
