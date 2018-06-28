package sgtmelon.handynotes.app.ui.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.adapter.AdapterPager;
import sgtmelon.handynotes.app.data.DataRoom;
import sgtmelon.handynotes.office.def.DefPages;
import sgtmelon.handynotes.office.def.data.DefType;
import sgtmelon.handynotes.office.intf.IntfMenu;
import sgtmelon.handynotes.app.model.state.StateNote;
import sgtmelon.handynotes.app.data.DataInfo;
import sgtmelon.handynotes.app.control.menu.MenuMain;
import sgtmelon.handynotes.app.model.manager.ManagerRoll;
import sgtmelon.handynotes.app.model.manager.ManagerStatus;
import sgtmelon.handynotes.app.ui.frg.FrgBin;
import sgtmelon.handynotes.app.ui.frg.FrgNote;
import sgtmelon.handynotes.app.ui.frg.FrgRank;

public class ActMain extends AppCompatActivity implements IntfMenu.MainClick {

    //TODO: введи вторую ветвь для работы с БД

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    final String TAG = "ActMain";

    public ManagerRoll managerRoll;
    public ManagerStatus managerStatus;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        DataRoom db = DataRoom.provideDb(this);
        managerRoll = db.daoRoll().getManagerRoll();
        managerStatus = db.daoNote().getManagerStatus(this);
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Log.i(TAG, "onCreate");

        setupViewPager();
        setupMenuMain();
    }

    //region Variable
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    public FrgRank frgRank;
    public FrgNote frgNote;
    public FrgBin frgBin;
    //endregion

    private void setupViewPager() {
        Log.i(TAG, "setupViewPager");

        viewPager = findViewById(R.id.actMain_vp);
        bottomNavigationView = findViewById(R.id.actMain_bnv_menu);

        FragmentManager fragmentManager = getSupportFragmentManager();
        AdapterPager adapterPager = new AdapterPager(fragmentManager);

        frgRank = new FrgRank();
        frgNote = new FrgNote();
        frgBin = new FrgBin();

        adapterPager.addFragment(frgRank);
        adapterPager.addFragment(frgNote);
        adapterPager.addFragment(frgBin);

        viewPager.setAdapter(adapterPager);
        viewPager.setOffscreenPageLimit(adapterPager.getCount() - 1);
    }

    private MenuMain menuMain;

    private void setupMenuMain() {
        Log.i(TAG, "setupMenuMain");

        menuMain = new MenuMain(viewPager, bottomNavigationView);
        menuMain.setMainClick(this);

        viewPager.addOnPageChangeListener(menuMain);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuMain);

        menuMain.setPage(DefPages.notes);
    }

    @Override
    public void onMenuNoteClick() {
        Log.i(TAG, "onMenuNoteClick");

        String[] itemAddOpt = getResources().getStringArray(R.array.dialog_menu_add);
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog);
        alert.setTitle(getString(R.string.dialog_title_add_note))
                .setItems(itemAddOpt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent = new Intent(ActMain.this, ActNote.class);

                        intent.putExtra(StateNote.KEY_CREATE, true);
                        intent.putExtra(DataInfo.NT_TP, item);
                        intent.putExtra(DataInfo.RK_VS, frgRank.managerRank.getVisible());

                        startActivity(intent);
                    }
                }).setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (menuMain.getPageCurrent() == MenuMain.pageRank && ev.getAction() == MotionEvent.ACTION_DOWN) {
//            Log.i(TAG, "dispatchTouchEvent");
//
//            View view = getCurrentFocus();
//            if (view != null) {
//                View viewTmp = getCurrentFocus();
//                View viewNew = viewTmp != null ? viewTmp : view;
//
//                if (viewNew.equals(view)) {
//                    Rect rect = new Rect();
//                    int[] coordinate = new int[2];
//
//                    view.getLocationOnScreen(coordinate);
//                    rect.set(coordinate[0], coordinate[1], coordinate[0] + view.getWidth(), coordinate[1] + view.getHeight());
//
//                    final int x = (int) ev.getX();
//                    final int y = (int) ev.getY();
//
//                    if (rect.contains(x, y)) return super.dispatchTouchEvent(ev);
//                }
//
//                Help.hideKeyboard(this, viewNew);
//                viewNew.clearFocus();
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");

        int buttonCurrent = menuMain.getPageCurrent();

        if (buttonCurrent != DefPages.notes) {
            switch (buttonCurrent) {
                case DefPages.rank:
                    if (frgRank.managerRank.needClearEnter()) frgRank.managerRank.clearEnter();
                    else menuMain.setPage(DefPages.notes);
                    break;
                default:
                    menuMain.setPage(DefPages.notes);
                    break;
            }
        } else super.onBackPressed();
    }

}