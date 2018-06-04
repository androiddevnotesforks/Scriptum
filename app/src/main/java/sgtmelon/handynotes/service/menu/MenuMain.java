package sgtmelon.handynotes.service.menu;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.interfaces.menu.MenuMainClick;

public class MenuMain implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    //region Variables
    private int[] itemId = new int[]{
            R.id.menu_main_pageRank,
            R.id.menu_main_pageNotes,
            R.id.menu_main_pageBin,
    };

    public static final int pageRank = 0;
    public static final int pageNotes = 1;
    private static final int pageBin = 2;

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    //endregion

    public MenuMain(ViewPager viewPager, BottomNavigationView bottomNavigationView) {
        this.viewPager = viewPager;
        this.bottomNavigationView = bottomNavigationView;
    }

    public void setPage(int page) {
        bottomNavigationView.setSelectedItemId(itemId[page]);
    }

    private MenuMainClick menuMainClick;

    public void setMenuMainClick(MenuMainClick menuMainClick) {
        this.menuMainClick = menuMainClick;
    }

    private boolean needChange = false;     //Для избежания повторного вызова метода setPageCurrent после нажатия на кнопку
    private int pageCurrent;

    public int getPageCurrent() {
        return pageCurrent;
    }

    private void setPageCurrent(int navId) {
        switch (navId) {
            case R.id.menu_main_pageRank:
                pageCurrent = pageRank;
                break;
            case R.id.menu_main_pageNotes:
                if (pageCurrent == pageNotes) menuMainClick.onMenuNoteClick();
                else pageCurrent = pageNotes;
                break;
            case R.id.menu_main_pageBin:
                pageCurrent = pageBin;
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (needChange) bottomNavigationView.setSelectedItemId(itemId[position]);
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