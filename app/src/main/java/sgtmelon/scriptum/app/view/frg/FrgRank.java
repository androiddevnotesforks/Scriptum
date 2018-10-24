package sgtmelon.scriptum.app.view.frg;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.AdpRank;
import sgtmelon.scriptum.app.dataBase.DbRoom;
import sgtmelon.scriptum.app.injection.component.ComFrg;
import sgtmelon.scriptum.app.injection.component.DaggerComFrg;
import sgtmelon.scriptum.app.injection.module.ModBlankFrg;
import sgtmelon.scriptum.app.model.item.ItemRank;
import sgtmelon.scriptum.app.model.repo.RepoRank;
import sgtmelon.scriptum.app.viewModel.VmFrgRank;
import sgtmelon.scriptum.databinding.FrgRankBinding;
import sgtmelon.scriptum.element.dialog.DlgRename;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.intf.IntfItem;
import sgtmelon.scriptum.office.st.StDrag;
import sgtmelon.scriptum.office.st.StOpen;

public class FrgRank extends Fragment implements IntfItem.Click, IntfItem.LongClick,
        View.OnClickListener, View.OnLongClickListener {

    //region Variable
    private static final String TAG = "FrgRank";

    private DbRoom db;

    @Inject
    Context context;
    @Inject
    FragmentManager fm;

    @Inject
    FrgRankBinding binding;
    @Inject
    VmFrgRank vm;

    private View frgView;
    //endregion

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        updateAdapter();
        tintButton();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        ComFrg comFrg = DaggerComFrg.builder()
                .modBlankFrg(new ModBlankFrg(this, inflater, container))
                .build();
        comFrg.inject(this);

        vm.loadData();

        if (savedInstanceState != null) stOpen.setOpen(savedInstanceState.getBoolean(DefDlg.OPEN));

        return frgView = binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");

        setupToolbar();
        setupRecycler();
    }

    private void bind(int listSize) {
        binding.setListEmpty(listSize == 0);
        binding.executePendingBindings();
    }

    private ImageButton rankCancel;
    private ImageButton rankAdd;
    private EditText rankEnter;

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_frg_rank));

        rankCancel = frgView.findViewById(R.id.incToolbarRank_ib_cancel);
        rankAdd = frgView.findViewById(R.id.incToolbarRank_ib_add);
        rankEnter = frgView.findViewById(R.id.incToolbarRank_et_enter);

        rankEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tintButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rankEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                String name = rankEnter.getText().toString().toUpperCase();

                if (!name.equals("") && !vm.getRepoRank().getListName().contains(name)) {
                    onClick(rankAdd);
                }
                return true;
            }
            return false;
        });

        rankCancel.setOnClickListener(this);
        rankAdd.setOnClickListener(this);
        rankAdd.setOnLongClickListener(this);
    }

    private void tintButton() {
        String name = rankEnter.getText().toString().toUpperCase();

        Help.Tint.button(context, rankCancel, R.drawable.ic_cancel_on, R.attr.clIcon, name);
        Help.Tint.button(context, rankAdd, R.drawable.ic_rank, name, !vm.getRepoRank().getListName().contains(name));
    }

    private String clearEnter() {
        String name = rankEnter.getText().toString();
        rankEnter.setText("");
        return name;
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");

        switch (view.getId()) {
            case R.id.incToolbarRank_ib_cancel:
                clearEnter();
                break;
            case R.id.incToolbarRank_ib_add:
                RepoRank repoRank = vm.getRepoRank();

                int rankPs = repoRank.size();
                String name = clearEnter();

                ItemRank itemRank = new ItemRank(rankPs, name);

                db = DbRoom.provideDb(context);
                long rankId = db.daoRank().insert(itemRank);
                db.close();

                itemRank.setId(rankId);
                repoRank.add(rankPs, itemRank);

                vm.setRepoRank(repoRank);
                adapter.update(repoRank.getListRank());

                if (repoRank.size() == 1) {
                    bind(repoRank.size());
                    adapter.notifyItemInserted(rankPs);
                } else {
                    if (layoutManager.findLastVisibleItemPosition() == rankPs - 1) {    //Если видимая позиция равна позиции куда добавили заметку
                        recyclerView.scrollToPosition(rankPs);                          //Прокручиваем до края, незаметно
                        adapter.notifyItemInserted(rankPs);                             //Добавляем элемент с анимацией
                    } else {
                        recyclerView.smoothScrollToPosition(rankPs);                    //Медленно прокручиваем, через весь список
                        adapter.notifyDataSetChanged();                                 //Добавляем элемент без анимации
                    }
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Log.i(TAG, "onLongClick");

        int ps = 0;
        String name = clearEnter();

        ItemRank itemRank = new ItemRank(ps - 1, name);

        db = DbRoom.provideDb(context);
        long rankId = db.daoRank().insert(itemRank);
        db.daoRank().update(ps);
        db.close();

        itemRank.setId(rankId);

        RepoRank repoRank = vm.getRepoRank();
        repoRank.add(ps, itemRank);
        vm.setRepoRank(repoRank);

        adapter.update(repoRank.getListRank());

        if (repoRank.size() == 1) bind(repoRank.size());
        else {
            if (layoutManager.findFirstVisibleItemPosition() == ps) {   //Если видимая позиция равна позиции куда добавили заметку
                recyclerView.scrollToPosition(ps);                      //Прокручиваем до края, незаметно
                adapter.notifyItemInserted(ps);                         //Добавляем элемент с анимацией
            } else {
                recyclerView.smoothScrollToPosition(ps);                //Медленно прокручиваем, через весь список
                adapter.notifyDataSetChanged();                         //Добавляем элемент без анимации
            }
        }
        return true;
    }

    //region Recycler variable
    private RecyclerView recyclerView;

    @Inject
    StDrag stDrag;

    @Inject
    LinearLayoutManager layoutManager;
    @Inject
    AdpRank adapter;

    @Inject
    StOpen stOpen;
    @Inject
    DlgRename dlgRename;
    //endregion

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind(vm.getRepoRank().size());
            }
        };

        adapter.setCallback(this, this, stDrag);

        recyclerView = frgView.findViewById(R.id.frgRank_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        dlgRename.setPositiveListener((dialogInterface, i) -> {
            int p = dlgRename.getPosition();

            RepoRank repoRank = vm.getRepoRank();
            ItemRank itemRank = repoRank.get(p);
            itemRank.setName(dlgRename.getName());

            db = DbRoom.provideDb(context);
            db.daoRank().update(itemRank);
            db.close();

            repoRank.set(p, itemRank);

            tintButton();

            vm.setRepoRank(repoRank);

            adapter.update(p, itemRank);
            adapter.notifyItemChanged(p);
        });
        dlgRename.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");
        Log.i(TAG, "updateAdapter: vm isNull: " + (vm == null));

        RepoRank repoRank = vm.loadData();

        adapter.update(repoRank.getListRank());
        adapter.notifyDataSetChanged();

        bind(repoRank.size());
    }

    @Override
    public void onItemClick(View view, final int p) {
        Log.i(TAG, "onItemClick");

        final RepoRank repoRank = vm.getRepoRank();
        final ItemRank itemRank = repoRank.get(p);

        switch (view.getId()) {
            case R.id.itemRank_bv_visible:
                itemRank.setVisible(!itemRank.isVisible());

                repoRank.set(p, itemRank);

                vm.setRepoRank(repoRank);
                adapter.update(p, itemRank);

                db = DbRoom.provideDb(context);
                db.daoRank().update(itemRank);
                db.daoNote().update(context);
                db.close();
                break;
            case R.id.itemRank_ll_click:
                if (!stOpen.isOpen()) {
                    stOpen.setOpen();

                    dlgRename.setArguments(p, itemRank.getName(), new ArrayList<>(repoRank.getListName()));
                    dlgRename.show(fm, DefDlg.RENAME);
                }
                break;
            case R.id.itemRank_ib_cancel:
                db = DbRoom.provideDb(context);
                db.daoRank().delete(repoRank.get(p).getName());
                db.daoRank().update(p);
                db.daoNote().update(context);
                db.close();

                repoRank.remove(p);

                vm.setRepoRank(repoRank);

                adapter.update(repoRank.getListRank());
                adapter.notifyItemRemoved(p);
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        RepoRank repoRank = vm.getRepoRank();

        boolean[] iconStartAnim = adapter.getStartAnim();
        boolean clickVisible = repoRank.get(p).isVisible();

        for (int i = 0; i < repoRank.size(); i++) {
            if (i != p) {
                ItemRank itemRank = repoRank.get(i);
                boolean isVisible = itemRank.isVisible();

                if (clickVisible == isVisible) {
                    iconStartAnim[i] = true;
                    itemRank.setVisible(!isVisible);
                    repoRank.set(i, itemRank);
                }
            }
        }

        List<ItemRank> listRank = repoRank.getListRank();

        vm.setRepoRank(repoRank);

        adapter.update(listRank, iconStartAnim);
        adapter.notifyDataSetChanged();

        db = DbRoom.provideDb(context);
        db.daoRank().updateRank(listRank);
        db.daoNote().update(context);
        db.close();
    }

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {

        private int dragStart;
        private int dragEnd;

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int flagsDrag = stDrag.isDrag()
                    ? ItemTouchHelper.UP | ItemTouchHelper.DOWN
                    : 0;

            int flagsSwipe = 0;

            return makeMovementFlags(flagsDrag, flagsSwipe);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);

            switch (actionState) {
                case ItemTouchHelper.ACTION_STATE_DRAG:
                    dragStart = viewHolder.getAdapterPosition();
                    break;
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            dragEnd = viewHolder.getAdapterPosition();
            if (dragStart != dragEnd) {
                db = DbRoom.provideDb(context);
                List<ItemRank> listRank = db.daoRank().update(dragStart, dragEnd);
                db.daoNote().update(context);
                db.close();

                RepoRank repoRank = vm.getRepoRank();
                repoRank.setListRank(listRank);
                vm.setRepoRank(repoRank);

                adapter.update(listRank);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPs = viewHolder.getAdapterPosition();
            int newPs = target.getAdapterPosition();

            RepoRank repoRank = vm.getRepoRank();
            repoRank.move(oldPs, newPs);
            vm.setRepoRank(repoRank);

            adapter.update(repoRank.getListRank());
            adapter.notifyItemMoved(oldPs, newPs);

            return true;
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(DefDlg.OPEN, stOpen.isOpen());
    }

}