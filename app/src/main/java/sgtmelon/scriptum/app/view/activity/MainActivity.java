package sgtmelon.scriptum.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import sgtmelon.safedialog.library.SheetDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.factory.DialogFactory;
import sgtmelon.scriptum.app.factory.FragmentFactory;
import sgtmelon.scriptum.app.view.callback.MainCallback;
import sgtmelon.scriptum.app.view.fragment.BinFragment;
import sgtmelon.scriptum.app.view.fragment.NotesFragment;
import sgtmelon.scriptum.app.view.fragment.RankFragment;
import sgtmelon.scriptum.app.view.parent.BaseActivityParent;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.FragmentDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.PageDef;
import sgtmelon.scriptum.office.annot.def.TypeNoteDef;
import sgtmelon.scriptum.office.st.OpenSt;
import sgtmelon.scriptum.office.st.PageSt;


public final class MainActivity extends BaseActivityParent implements MainCallback,
        BottomNavigationView.OnNavigationItemSelectedListener {

    // TODO: 28.01.2019 перевести приложение на Kotlin + RxJava + Spek


    // TODO: 13.01.2019 Annotation NonNull/Nullable везде где только можно (для override методов добавить nullable)
    // TODO: 13.01.2019 Добавить getAdapterPosition safety - RecyclerView.NO_POSITION check
    // TODO: 16.01.2019 сделать блокировку кнопки изменить сохранить при работе анимации крестик-стрелка (если анимируется - не нажимать)
    // TODO: 19.01.2019 Добавить перескакивание курсора при старте редактирования в нужное место
    // TODO: 20.01.2019 Разобраться со стилями
    // TODO: 27.01.2019 Добавить ещё одну тему
    // TODO: 22.11.2018 аннотация профессор

    private static final String TAG = MainActivity.class.getSimpleName();

    private final PageSt pageSt = new PageSt();
    private final OpenSt openSt = new OpenSt();

    private FragmentManager fm;
    private RankFragment rankFragment;
    private NotesFragment notesFragment;
    private BinFragment binFragment;
    private SheetDialog sheetDialog;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        rankFragment = FragmentFactory.INSTANCE.getRankFragment(fm);
        notesFragment = FragmentFactory.INSTANCE.getNotesFragment(fm);
        binFragment = FragmentFactory.INSTANCE.getBinFragment(fm);

        sheetDialog = DialogFactory.INSTANCE.getSheetDialog(fm);

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

        fab = findViewById(R.id.main_add_fab);
        fab.setOnClickListener(view -> {
            if (!openSt.isOpen()) {
                openSt.setOpen(true);

                sheetDialog.setArguments(R.layout.sheet_add, R.id.add_navigation);
                sheetDialog.show(fm, DialogDef.SHEET);
            }
        });

        final BottomNavigationView navigationView = findViewById(R.id.main_menu_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(PageDef.itemId[page]);

        sheetDialog.setNavigationItemSelectedListener(menuItem -> {
            sheetDialog.dismiss();

            final int type = menuItem.getItemId() == R.id.item_add_text
                    ? TypeNoteDef.text
                    : TypeNoteDef.roll;

            final Intent intent = NoteActivity.getIntent(MainActivity.this, type);

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

        final int pageOld = pageSt.getPage();
        int page = pageSt.getPage();

        switch (menuItem.getItemId()) {
            case R.id.item_page_rank:
                page = PageDef.rank;
                break;
            case R.id.item_page_notes:
                page = PageDef.notes;
                break;
            case R.id.item_page_bin:
                page = PageDef.bin;
                break;
        }
        boolean scrollTop = page == pageSt.getPage();

        pageSt.setPage(page);

        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        switch (pageSt.getPage()) {
            case PageDef.rank:
                if (scrollTop) {
                    rankFragment.scrollTop();
                } else {
                    changeFabState(false);
                    if (fm.findFragmentByTag(FragmentDef.RANK) != null) {
                        transaction.show(rankFragment);
                        rankFragment.onResume();
                    } else {
                        transaction.add(R.id.main_fragment_container, rankFragment, FragmentDef.RANK);
                    }
                }
                break;
            case PageDef.notes:
                if (scrollTop) {
                    notesFragment.scrollTop();
                } else {
                    changeFabState(true);
                    if (fm.findFragmentByTag(FragmentDef.NOTES) != null) {
                        transaction.show(notesFragment);
                        notesFragment.onResume();
                    } else {
                        transaction.add(R.id.main_fragment_container, notesFragment, FragmentDef.NOTES);
                    }
                }
                break;
            case PageDef.bin:
                if (scrollTop) {
                    binFragment.scrollTop();
                } else {
                    changeFabState(false);
                    if (fm.findFragmentByTag(FragmentDef.BIN) != null) {
                        transaction.show(binFragment);
                        binFragment.onResume();
                    } else {
                        transaction.add(R.id.main_fragment_container, binFragment, FragmentDef.BIN);
                    }
                }
                break;
        }

        if (!scrollTop) {
            switch (pageOld) {
                case PageDef.rank:
                    if (fm.findFragmentByTag(FragmentDef.RANK) != null) {
                        transaction.hide(rankFragment);
                    }
                    break;
                case PageDef.notes:
                    if (fm.findFragmentByTag(FragmentDef.NOTES) != null) {
                        transaction.hide(notesFragment);
                    }
                    break;
                case PageDef.bin:
                    if (fm.findFragmentByTag(FragmentDef.BIN) != null) {
                        transaction.hide(binFragment);
                    }
                    break;
            }
        }

        transaction.commit();
        return true;
    }

}