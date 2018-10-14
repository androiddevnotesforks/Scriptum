package sgtmelon.scriptum.app.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import sgtmelon.scriptum.app.view.activity.IntroActivity;
import sgtmelon.scriptum.app.view.fragment.IntroFragment;

/**
 * Адаптер для вступления {@link IntroActivity}
 */
public final class PagerAdapter extends FragmentPagerAdapter {

    private final List<IntroFragment> itemList = new ArrayList<>();

    public PagerAdapter(FragmentManager fm) {
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
