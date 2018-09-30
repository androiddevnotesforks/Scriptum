package sgtmelon.scriptum.app.view.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.component.ComAct;
import sgtmelon.scriptum.app.injection.component.DaggerComAct;
import sgtmelon.scriptum.app.injection.module.ModBlankAct;
import sgtmelon.scriptum.app.view.frg.FrgBin;
import sgtmelon.scriptum.app.view.frg.FrgNotes;
import sgtmelon.scriptum.app.view.frg.FrgRank;
import sgtmelon.scriptum.element.dialog.common.DlgSheet;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefFrg;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.annot.def.DefPage;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.blank.BlankAct;
import sgtmelon.scriptum.office.st.StNote;
import sgtmelon.scriptum.office.st.StOpen;
import sgtmelon.scriptum.office.st.StPage;

public class ActMain extends BlankAct implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = ActMain.class.getSimpleName();

    @Inject
    FragmentManager fm;

    @Inject
    StPage stPage = new StPage();
    @Inject
    StOpen stOpen = new StOpen();

    @Inject
    FrgRank frgRank;
    @Inject
    FrgNotes frgNotes;
    @Inject
    FrgBin frgBin;

    @Inject
    DlgSheet dlgSheetAdd;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        ComAct comAct = DaggerComAct.builder().modBlankAct(new ModBlankAct(this, this)).build();
        comAct.inject(this);

        int page = DefPage.notes;
        if (savedInstanceState != null) {
            stPage = savedInstanceState.getParcelable(DefIntent.STATE_PAGE);
            stOpen = savedInstanceState.getParcelable(DefIntent.STATE_OPEN);

            page = stPage.getPage();
        }

        setupNavigation(page);
    }

    private void setupNavigation(@DefPage int page) {
        Log.i(TAG, "setupNavigation");

        fab = findViewById(R.id.actMain_fab);
        fab.setOnClickListener(view -> {
            if (stOpen.isNotOpen()) {
                stOpen.setOpen(true);

                dlgSheetAdd.show(fm, DefDlg.SHEET_ADD);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.actMain_bnv_menu);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(DefPage.itemId[page]);

        dlgSheetAdd.setNavigationItemSelectedListener(menuItem -> {
            dlgSheetAdd.dismiss();

            @DefType int ntType = menuItem.getItemId() == R.id.menu_sheetAdd_text
                    ? DefType.text
                    : DefType.roll;

            Intent intent = new Intent(ActMain.this, ActNote.class);
            intent.putExtra(DefIntent.STATE_NOTE, new StNote(true, false));
            intent.putExtra(DefIntent.NOTE_TYPE, ntType);

            startActivity(intent);
            return true;
        });
        dlgSheetAdd.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i(TAG, "onNavigationItemSelected");

        boolean scroll = stPage.setPage(menuItem.getItemId());
        if (scroll) {
            switch (stPage.getPage()) {
                case DefPage.rank:
                    frgRank.recyclerView.smoothScrollToPosition(0);
                    break;
                case DefPage.notes:
                    frgNotes.recyclerView.smoothScrollToPosition(0);
                    break;
                case DefPage.bin:
                    frgBin.recyclerView.smoothScrollToPosition(0);
                    break;
            }
        } else {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            switch (stPage.getPage()) {
                case DefPage.rank:
                    fab.setEnabled(false);
                    fab.hide();
                    transaction.replace(R.id.actMain_fl_container, frgRank, DefFrg.RANK);
                    break;
                case DefPage.notes:
                    fab.setEnabled(true);
                    fab.show();
                    transaction.replace(R.id.actMain_fl_container, frgNotes, DefFrg.NOTES);
                    break;
                case DefPage.bin:
                    fab.setEnabled(false);
                    fab.hide();
                    transaction.replace(R.id.actMain_fl_container, frgBin, DefFrg.BIN);
                    break;
            }
            transaction.commit();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putParcelable(DefIntent.STATE_PAGE, stPage);
        outState.putParcelable(DefIntent.STATE_OPEN, stOpen);
    }

}