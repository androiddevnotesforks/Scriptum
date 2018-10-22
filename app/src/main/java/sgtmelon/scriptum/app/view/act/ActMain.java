package sgtmelon.scriptum.app.view.act;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.view.frg.FrgBin;
import sgtmelon.scriptum.app.view.frg.FrgNotes;
import sgtmelon.scriptum.app.view.frg.FrgRank;
import sgtmelon.scriptum.app.injection.component.ComAct;
import sgtmelon.scriptum.app.injection.component.DaggerComAct;
import sgtmelon.scriptum.app.injection.module.ModBlankAct;
import sgtmelon.scriptum.element.dialog.DlgSheetAdd;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefFrg;
import sgtmelon.scriptum.office.annot.def.DefNote;
import sgtmelon.scriptum.office.annot.def.DefPage;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.blank.BlankAct;
import sgtmelon.scriptum.office.st.StPage;

public class ActMain extends BlankAct implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    //region Variable
    private static final String TAG = "ActMain";

    @Inject
    FragmentManager fm;

    @Inject
    StPage stPage;

    @Inject
    FrgRank frgRank;
    @Inject
    FrgNotes frgNotes;
    @Inject
    FrgBin frgBin;

    @Inject
    DlgSheetAdd dlgSheetAdd;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Log.i(TAG, "onCreate");

        ComAct comAct = DaggerComAct.builder().modBlankAct(new ModBlankAct(this, this)).build();
        comAct.inject(this);

        setupNavigation(savedInstanceState != null
                ? savedInstanceState.getInt(DefPage.PAGE)
                : DefPage.notes);
    }

    private void setupNavigation(@DefPage int page) {
        Log.i(TAG, "setupNavigation");

        BottomNavigationView navigationView = findViewById(R.id.actMain_bnv_menu);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(DefPage.itemId[page]);

        dlgSheetAdd.setNavigationItemSelectedListener(menuItem -> {
            dlgSheetAdd.dismiss();

            Intent intent = new Intent(ActMain.this, ActNote.class);

            intent.putExtra(DefNote.CREATE, true);
            intent.putExtra(DefNote.TYPE, menuItem.getItemId() == R.id.menu_sheetAdd_text
                    ? DefType.text
                    : DefType.roll);

            startActivity(intent);
            return true;
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i(TAG, "onNavigationItemSelected");

        boolean add = stPage.setPage(menuItem.getItemId());
        if (add) {
            if (!dlgSheetAdd.isVisible()) dlgSheetAdd.show(fm, DefDlg.SHEET_ADD);
        } else {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            switch (stPage.getPage()) {
                case DefPage.rank:
                    transaction.replace(R.id.actMain_fl_container, frgRank, DefFrg.RANK);
                    break;
                case DefPage.notes:
                    transaction.replace(R.id.actMain_fl_container, frgNotes, DefFrg.NOTES);
                    break;
                case DefPage.bin:
                    transaction.replace(R.id.actMain_fl_container, frgBin, DefFrg.BIN);
                    break;
            }
            transaction.commit();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");

        outState.putInt(DefPage.PAGE, stPage.getPage());
    }

}