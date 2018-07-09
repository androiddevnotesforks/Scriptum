package sgtmelon.handynotes.app.view.act;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.util.Log;
import android.view.MenuItem;

import androidx.fragment.app.FragmentTransaction;
import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.view.frg.FrgBin;
import sgtmelon.handynotes.app.view.frg.FrgNotes;
import sgtmelon.handynotes.app.view.frg.FrgRank;
import sgtmelon.handynotes.element.SheetAdd;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.Frg;
import sgtmelon.handynotes.office.annot.def.DefPage;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.st.StPage;

public class ActMain extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final String TAG = "ActMain";

    private StPage stPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        Log.i(TAG, "onCreate");

        stPage = new StPage();

        setupNavigation(savedInstanceState != null
                ? savedInstanceState.getInt(DefPage.PAGE)
                : DefPage.notes);
    }

    private FragmentManager manager;

    private FrgRank frgRank;
    private FrgNotes frgNotes;
    private FrgBin frgBin;

    private SheetAdd sheetAdd;

    private void setupNavigation(@DefPage int page) {
        Log.i(TAG, "setupNavigation");

        manager = getSupportFragmentManager();

        frgRank = (FrgRank) manager.findFragmentByTag(Frg.RANK);
        if (frgRank == null) frgRank = new FrgRank();

        frgNotes = (FrgNotes) manager.findFragmentByTag(Frg.NOTES);
        if (frgNotes == null) frgNotes = new FrgNotes();

        frgBin = (FrgBin) manager.findFragmentByTag(Frg.BIN);
        if (frgBin == null) frgBin = new FrgBin();

        BottomNavigationView navigationView = findViewById(R.id.actMain_bnv_menu);
        navigationView.setOnNavigationItemSelectedListener(this);

        navigationView.setSelectedItemId(DefPage.itemId[page]);

        sheetAdd = new SheetAdd();
        sheetAdd.setNavigationItemSelectedListener(menuItem -> {
            sheetAdd.dismiss();

            Intent intent = new Intent(ActMain.this, ActNote.class);

            intent.putExtra(DefPage.CREATE, true);
            intent.putExtra(Db.NT_TP, menuItem.getItemId() == R.id.menu_sheetAdd_text
                    ? DefType.text
                    : DefType.roll);

            startActivity(intent);
            return true;
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Log.i(TAG, "onNavigationItemSelected");

        boolean add = stPage.setPage(menuItem.getItemId());
        if (add) {
            if(!sheetAdd.isVisible()) sheetAdd.show(manager, sheetAdd.getTag());
        } else {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            switch (stPage.getPage()) {
                case DefPage.rank:
                    transaction.replace(R.id.actMain_fl_container, frgRank, Frg.RANK);
                    break;
                case DefPage.notes:
                    transaction.replace(R.id.actMain_fl_container, frgNotes, Frg.NOTES);
                    break;
                case DefPage.bin:
                    transaction.replace(R.id.actMain_fl_container, frgBin, Frg.BIN);
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