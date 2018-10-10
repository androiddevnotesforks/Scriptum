package sgtmelon.handynotes.app.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdpPager extends FragmentPagerAdapter {

    private final List<Fragment> pageList = new ArrayList<>();

    public AdpPager(FragmentManager fm) {
        super(fm);
    }

    public void addItem(Fragment fragment) {
        pageList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return pageList.get(position);
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

}