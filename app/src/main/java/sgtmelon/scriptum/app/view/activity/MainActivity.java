package sgtmelon.scriptum.app.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
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
import sgtmelon.scriptum.office.annot.def.TypeNoteDef;
import sgtmelon.scriptum.office.st.OpenSt;
import sgtmelon.scriptum.office.st.PageSt;

// TODO: 22.11.2018 аннотация профессор

public final class MainActivity extends BaseActivityParent implements MainCallback,
        BottomNavigationView.OnNavigationItemSelectedListener {

    // TODO: 13.01.2019 Annotation NonNull/Nullable везде где только можно (для override методов добавить nullable)
    // TODO: 13.01.2019 Добавить getAdapterPosition safety - RecyclerView.NO_POSITION check
    // TODO: 16.01.2019 сделать блокировку кнопки изменить сохранить при работе анимации крестик-стрелка
    // TODO: 19.01.2019 Добавить перескакивание курсора при старте редактирования в нужное место
    // TODO: 20.01.2019 Разобраться со стилями

    private static final String TAG = MainActivity.class.getSimpleName();

    private final PageSt pageSt = new PageSt();
    private final OpenSt openSt = new OpenSt();

    @Inject FragmentManager fm;
    @Inject RankFragment rankFragment;
    @Inject NotesFragment notesFragment;
    @Inject BinFragment binFragment;
    @Inject SheetDialog sheetDialog;
    boolean revealShow = false;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .activityBlankModule(new ActivityBlankModule(this))
                .build();
        activityComponent.inject(this);

        int page = PageDef.notes;
        if (savedInstanceState != null) {
            pageSt.setPage(savedInstanceState.getInt(IntentDef.STATE_PAGE));
            openSt.setOpen(savedInstanceState.getBoolean(IntentDef.STATE_OPEN));

            page = pageSt.getPage();
        }

        final View view = findViewById(R.id.reveal_container);
        view.setOnClickListener((v) -> {
            if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) return;

            final int cx = (view.getLeft() + view.getRight()) / 2;
            final int cy = (view.getTop() + view.getBottom()) / 2;

            final float maxRadius = (float) Math.hypot(view.getWidth(), view.getHeight());
            final float minRadius = 0;

            final Animator animator;

            animator = ViewAnimationUtils.createCircularReveal(view, cx, cy,
                    revealShow ? maxRadius : minRadius,
                    revealShow ? minRadius : maxRadius
            );

            animator.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
            animator.setInterpolator(new FastOutSlowInInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (!revealShow) {
                        view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.accent_dark));
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (revealShow) {
                        view.setBackgroundColor(Color.TRANSPARENT);
                    }

                    revealShow = !revealShow;
                }
            });

            animator.start();
        });

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

        final BottomNavigationView navigationView = findViewById(R.id.menu_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(PageDef.itemId[page]);

        sheetDialog.setNavigationItemSelectedListener(menuItem -> {
            sheetDialog.dismiss();

            final int type = menuItem.getItemId() == R.id.note_text_item
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
                        transaction.add(R.id.fragment_container, rankFragment, FragmentDef.RANK);
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
                        transaction.add(R.id.fragment_container, notesFragment, FragmentDef.NOTES);
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
                        transaction.add(R.id.fragment_container, binFragment, FragmentDef.BIN);
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