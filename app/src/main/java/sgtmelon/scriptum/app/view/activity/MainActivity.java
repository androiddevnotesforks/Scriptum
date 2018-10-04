package sgtmelon.scriptum.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.component.ComponentActivity;
import sgtmelon.scriptum.app.injection.component.DaggerComponentActivity;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankActivity;
import sgtmelon.scriptum.app.view.fragment.BinFragment;
import sgtmelon.scriptum.app.view.fragment.NotesFragment;
import sgtmelon.scriptum.app.view.fragment.RankFragment;
import sgtmelon.scriptum.element.common.DlgSheet;
import sgtmelon.scriptum.element.common.DlgSheet;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefFrg;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.annot.def.DefPage;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.blank.BlankAct;
import sgtmelon.scriptum.office.st.StOpen;
import sgtmelon.scriptum.office.st.StPage;

public final class MainActivity extends BlankAct implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final StPage stPage = new StPage();
    private final StOpen stOpen = new StOpen();

    @Inject
    FragmentManager fm;
    @Inject
    RankFragment rankFragment;
    @Inject
    NotesFragment notesFragment;
    @Inject
    BinFragment binFragment;

    @Inject
    DlgSheet dlgSheetAdd;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ComponentActivity componentActivity = DaggerComponentActivity.builder()
                .moduleBlankActivity(new ModuleBlankActivity(this))
                .build();
        componentActivity.inject(this);

        int page = DefPage.notes;
        if (savedInstanceState != null) {
            stPage.setPage(savedInstanceState.getInt(DefIntent.STATE_PAGE));
            stOpen.setOpen(savedInstanceState.getBoolean(DefIntent.STATE_OPEN));

            page = stPage.getPage();
        }

        setupNavigation(page);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putInt(DefIntent.STATE_PAGE, stPage.getPage());
        outState.putBoolean(DefIntent.STATE_OPEN, stOpen.isOpen());
    }

    private void setupNavigation(@DefPage int page) {
        Log.i(TAG, "setupNavigation");

        fab = findViewById(R.id.add_fab);
        fab.setOnClickListener(view -> {
            if (!stOpen.isOpen()) {
                stOpen.setOpen(true);

                dlgSheetAdd.show(fm, DefDlg.SHEET_ADD);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.menu_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(DefPage.itemId[page]);

        dlgSheetAdd.setNavigationItemSelectedListener(menuItem -> {
            dlgSheetAdd.dismiss();

            @DefType int type = menuItem.getItemId() == R.id.note_text_item
                    ? DefType.text
                    : DefType.roll;

            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.putExtra(DefIntent.NOTE_CREATE, true);
            intent.putExtra(DefIntent.NOTE_TYPE, type);

            startActivity(intent);
            return true;
        });
        dlgSheetAdd.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i(TAG, "onNavigationItemSelected");

        int page = stPage.getPage();
        switch (menuItem.getItemId()) {
            case R.id.page_rank_item:
                page = DefPage.rank;
                break;
            case R.id.page_notes_item:
                page = DefPage.notes;
                break;
            case R.id.page_bin_item:
                page = DefPage.bin;
                break;
        }
        stPage.setPage(page);

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        switch (stPage.getPage()) {
            case DefPage.rank:
                fab.setEnabled(false);
                fab.hide();
                transaction.replace(R.id.fragment_container, rankFragment, DefFrg.RANK);
                break;
            case DefPage.notes:
                fab.setEnabled(true);
                fab.show();
                transaction.replace(R.id.fragment_container, notesFragment, DefFrg.NOTES);
                break;
            case DefPage.bin:
                fab.setEnabled(false);
                fab.hide();
                transaction.replace(R.id.fragment_container, binFragment, DefFrg.BIN);
                break;
        }
        transaction.commit();

        return true;
    }

}