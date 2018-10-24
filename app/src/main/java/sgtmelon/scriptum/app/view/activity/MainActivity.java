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
import sgtmelon.safedialog.library.SheetDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.injection.component.ActivityComponent;
import sgtmelon.scriptum.app.injection.component.DaggerActivityComponent;
import sgtmelon.scriptum.app.injection.module.blank.ActivityBlankModule;
import sgtmelon.scriptum.app.view.callback.MainCallback;
import sgtmelon.scriptum.app.view.fragment.BinFragment;
import sgtmelon.scriptum.app.view.fragment.NotesFragment;
import sgtmelon.scriptum.app.view.fragment.RankFragment;
import sgtmelon.scriptum.app.view.parent.BaseActivityParent;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.FragmentDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.PageDef;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.st.OpenSt;
import sgtmelon.scriptum.office.st.PageSt;

public final class MainActivity extends BaseActivityParent implements MainCallback,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final PageSt pageSt = new PageSt();
    private final OpenSt openSt = new OpenSt();

    @Inject FragmentManager fm;
    @Inject RankFragment rankFragment;
    @Inject NotesFragment notesFragment;
    @Inject BinFragment binFragment;
    @Inject SheetDialog sheetDialog;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .activityBlankModule(new ActivityBlankModule(this))
                .build();
        activityComponent.inject(this);

        int page = PageDef.notes;
        if (savedInstanceState != null) {
            pageSt.setPage(savedInstanceState.getInt(IntentDef.STATE_PAGE));
            openSt.setOpen(savedInstanceState.getBoolean(IntentDef.STATE_OPEN));

            page = pageSt.getPage();
        }

        setupNavigation(page);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putInt(IntentDef.STATE_PAGE, pageSt.getPage());
        outState.putBoolean(IntentDef.STATE_OPEN, openSt.isOpen());
    }

    private void setupNavigation(@PageDef int page) {
        Log.i(TAG, "setupNavigation");

        fab = findViewById(R.id.add_fab);
        fab.setOnClickListener(view -> {
            if (!openSt.isOpen()) {
                openSt.setOpen(true);

                sheetDialog.setArguments(R.layout.sheet_add, R.id.add_navigation);
                sheetDialog.show(fm, DialogDef.SHEET);
            }
        });

        BottomNavigationView navigationView = findViewById(R.id.menu_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(PageDef.itemId[page]);

        sheetDialog.setNavigationItemSelectedListener(menuItem -> {
            sheetDialog.dismiss();

            @TypeDef int type = menuItem.getItemId() == R.id.note_text_item
                    ? TypeDef.text
                    : TypeDef.roll;

            Intent intent = NoteActivity.getIntent(MainActivity.this, type);

            startActivity(intent);
            return true;
        });
        sheetDialog.setDismissListener(dialogInterface -> openSt.setOpen(false));
    }

    @Override
    public void changeFabState(boolean show) {
        if (show) {
            fab.setEnabled(true);
            fab.show();
        } else {
            fab.setEnabled(false);
            fab.hide();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i(TAG, "onNavigationItemSelected");

        int page = pageSt.getPage();
        switch (menuItem.getItemId()) {
            case R.id.page_rank_item:
                page = PageDef.rank;
                break;
            case R.id.page_notes_item:
                page = PageDef.notes;
                break;
            case R.id.page_bin_item:
                page = PageDef.bin;
                break;
        }
        pageSt.setPage(page);

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        switch (pageSt.getPage()) {
            case PageDef.rank:
                changeFabState(false);
                transaction.replace(R.id.fragment_container, rankFragment, FragmentDef.RANK);
                break;
            case PageDef.notes:
                changeFabState(true);
                transaction.replace(R.id.fragment_container, notesFragment, FragmentDef.NOTES);
                break;
            case PageDef.bin:
                changeFabState(false);
                transaction.replace(R.id.fragment_container, binFragment, FragmentDef.BIN);
                break;
        }
        transaction.commit();

        return true;
    }

}