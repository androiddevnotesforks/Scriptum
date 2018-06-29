package sgtmelon.handynotes.app.control.menu;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.annot.def.DefPages;
import sgtmelon.handynotes.office.intf.IntfMenu;

public class MenuMain implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private final ViewPager viewPager;
    private final BottomNavigationView bottomNavigationView;

    public MenuMain(ViewPager viewPager, BottomNavigationView bottomNavigationView) {
        this.viewPager = viewPager;
        this.bottomNavigationView = bottomNavigationView;
    }

    public void setPage(@DefPages int page) {
        bottomNavigationView.setSelectedItemId(DefPages.itemId[page]);
    }

    private IntfMenu.MainClick mainClick;

    public void setMainClick(IntfMenu.MainClick mainClick) {
        this.mainClick = mainClick;
    }

    private boolean needChange = false;     //Для избежания повторного вызова метода setPageCurrent после нажатия на кнопку
    private int pageCurrent;

    public int getPageCurrent() {
        return pageCurrent;
    }

    private void setPageCurrent(int itemId) {
        switch (itemId) {
            case R.id.menu_actMain_pageRank:
                pageCurrent = DefPages.rank;
                break;
            case R.id.menu_actMain_pageNote:
                if (pageCurrent == DefPages.notes) mainClick.onMenuNoteClick();
                else pageCurrent = DefPages.notes;
                break;
            case R.id.menu_actMain_pageBin:
                pageCurrent = DefPages.bin;
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (needChange) bottomNavigationView.setSelectedItemId(DefPages.itemId[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (!needChange && state == ViewPager.SCROLL_STATE_DRAGGING) needChange = true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        needChange = false;
        setPageCurrent(item.getItemId());
        viewPager.setCurrentItem(pageCurrent, true);
        return true;
    }

}