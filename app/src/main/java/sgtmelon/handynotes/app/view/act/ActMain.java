package sgtmelon.handynotes.app.view.act;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
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
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.Frg;
import sgtmelon.handynotes.office.annot.def.DefPage;
import sgtmelon.handynotes.office.st.StPage;

public class ActMain extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

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
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Log.i(TAG, "onNavigationItemSelected");

        boolean add = stPage.setPage(menuItem.getItemId());
        if (add) {
            String[] itemAddOpt = getResources().getStringArray(R.array.dialog_menu_add);

            AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog);
            alert.setTitle(getString(R.string.dialog_title_add_note))
                    .setItems(itemAddOpt, (dialog, item) -> {
                        Intent intent = new Intent(ActMain.this, ActNote.class);

                        intent.putExtra(DefPage.CREATE, true);
                        intent.putExtra(Db.NT_TP, item);

                        startActivity(intent);
                    })
                    .setCancelable(true);

            AlertDialog dialog = alert.create();
            dialog.show();
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