package sgtmelon.handynotes.app.view.act;

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
import sgtmelon.handynotes.app.adapter.AdpPager;
import sgtmelon.handynotes.app.control.menu.MenuMain;
import sgtmelon.handynotes.app.view.frg.FrgBin;
import sgtmelon.handynotes.app.view.frg.FrgNotes;
import sgtmelon.handynotes.app.view.frg.FrgRank;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.DefPages;
import sgtmelon.handynotes.office.intf.IntfMenu;

public class ActMain extends AppCompatActivity implements IntfMenu.MainClick {

    //TODO: введи вторую ветвь для работы с БД

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final String TAG = "ActMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Log.i(TAG, "onCreate");

        setupViewPager();
        setupMenuMain(savedInstanceState == null ? DefPages.notes : savedInstanceState.getInt(DefPages.PAGE));
    }

    //region Variable
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    // FIXME: 05.07.2018 Убрать зависимости между фрагментами, переделать с помощью @LiveData

    private FrgRank frgRank;
    private FrgNotes frgNotes;
    private FrgBin frgBin;
    //endregion

    private void setupViewPager() {
        Log.i(TAG, "setupViewPager");

        viewPager = findViewById(R.id.actMain_vp);
        bottomNavigationView = findViewById(R.id.actMain_bnv_menu);

        FragmentManager manager = getSupportFragmentManager();

        frgRank = new FrgRank();
        frgNotes = new FrgNotes();
        frgBin = new FrgBin();

        AdpPager adapter = new AdpPager(manager);

        adapter.addFragment(frgRank);
        adapter.addFragment(frgNotes);
        adapter.addFragment(frgBin);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
    }

    private MenuMain menuMain;

    private void setupMenuMain(@DefPages int page) {
        Log.i(TAG, "setupMenuMain");

        menuMain = new MenuMain(viewPager, bottomNavigationView);
        menuMain.setMainClick(this);

        viewPager.addOnPageChangeListener(menuMain);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuMain);

        menuMain.setPage(page);
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

                        intent.putExtra(DefPages.CREATE, true);
                        intent.putExtra(Db.NT_TP, item);

                        startActivity(intent);
                    }
                }).setCancelable(true);
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    /**
     * @Override public boolean dispatchTouchEvent(MotionEvent ev) {
     * if (menuMain.getCurrent() == MenuMain.pageRank && ev.getAction() == MotionEvent.ACTION_DOWN) {
     * Log.i(TAG, "dispatchTouchEvent");
     * <p>
     * View view = getCurrentFocus();
     * if (view != null) {
     * View viewTmp = getCurrentFocus();
     * View viewNew = viewTmp != null ? viewTmp : view;
     * <p>
     * if (viewNew.equals(view)) {
     * Rect rect = new Rect();
     * int[] coordinate = new int[2];
     * <p>
     * view.getLocationOnScreen(coordinate);
     * rect.set(coordinate[0], coordinate[1], coordinate[0] + view.getWidth(), coordinate[1] + view.getHeight());
     * <p>
     * final int x = (int) ev.getX();
     * final int y = (int) ev.getY();
     * <p>
     * if (rect.contains(x, y)) return super.dispatchTouchEvent(ev);
     * }
     * <p>
     * Help.hideKeyboard(this, viewNew);
     * viewNew.clearFocus();
     * }
     * }
     * return super.dispatchTouchEvent(ev);
     * }
     */

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");

        int current = menuMain.getCurrent();

        if (current != DefPages.notes) {
            switch (current) {
                case DefPages.rank:
                    menuMain.setPage(DefPages.notes);
                    break;
                default:
                    menuMain.setPage(DefPages.notes);
                    break;
            }
        } else super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(DefPages.PAGE, menuMain.getCurrent());
    }
}