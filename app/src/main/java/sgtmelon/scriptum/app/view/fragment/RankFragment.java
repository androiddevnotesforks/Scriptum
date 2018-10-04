package sgtmelon.scriptum.app.view.fragment;

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
import sgtmelon.scriptum.app.adapter.AdapterRank;
import sgtmelon.scriptum.app.database.DbRoom;
import sgtmelon.scriptum.app.injection.component.ComponentFragment;
import sgtmelon.scriptum.app.injection.component.DaggerComponentFragment;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankFragment;
import sgtmelon.scriptum.app.model.ModelRank;
import sgtmelon.scriptum.app.model.item.ItemRank;
import sgtmelon.scriptum.app.vm.RankViewModel;
import sgtmelon.scriptum.databinding.FragmentRankBinding;
import sgtmelon.scriptum.element.DlgRename;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DefDlg;
import sgtmelon.scriptum.office.annot.def.DefIntent;
import sgtmelon.scriptum.office.intf.IntfItem;
import sgtmelon.scriptum.office.st.StDrag;
import sgtmelon.scriptum.office.st.StOpen;

public final class RankFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener,
        IntfItem.Click, IntfItem.LongClick {

    private static final String TAG = RankFragment.class.getSimpleName();

    private final StDrag stDrag = new StDrag();
    private final StOpen stOpen = new StOpen();

    private final AdapterRank adapter = new AdapterRank();

    @Inject
    FragmentManager fm;
    @Inject
    FragmentRankBinding binding;
    @Inject
    RankViewModel vm;
    @Inject
    DlgRename dlgRename;

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    private Context context;
    private DbRoom db;

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

                ModelRank modelRank = vm.getModelRank();
                modelRank.setListRank(listRank);
                vm.setModelRank(modelRank);

                adapter.setListRank(listRank);
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

            ModelRank modelRank = vm.getModelRank();
            modelRank.move(oldPs, newPs);
            vm.setModelRank(modelRank);

            adapter.setListRank(modelRank.getListRank());
            adapter.notifyItemMoved(oldPs, newPs);

            return true;
        }
    };

    private View frgView;
    private ImageButton rankCancel;
    private ImageButton rankAdd;
    private EditText rankEnter;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        updateAdapter();
        tintButton();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        ComponentFragment comFrg = DaggerComponentFragment.builder()
                .moduleBlankFragment(new ModuleBlankFragment(this, inflater, container))
                .build();
        comFrg.inject(this);

        frgView = binding.getRoot();

        if (savedInstanceState != null) {
            stOpen.setOpen(savedInstanceState.getBoolean(DefIntent.STATE_OPEN));
        }

        vm.loadData();

        return frgView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        setupToolbar();
        setupRecycler();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putBoolean(DefIntent.STATE_OPEN, stOpen.isOpen());
    }

    private void bind(int listSize) {
        Log.i(TAG, "bind");

        binding.setListEmpty(listSize == 0);
        binding.executePendingBindings();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_frg_rank));

        rankCancel = frgView.findViewById(R.id.cancel_button);
        rankAdd = frgView.findViewById(R.id.add_button);
        rankEnter = frgView.findViewById(R.id.rank_enter);

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

                if (!name.equals("") && !vm.getModelRank().getListName().contains(name)) {
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

    // TODO: 30.09.2018 переделай в отдельный класс
    private void tintButton() {
        Log.i(TAG, "tintButton");

        String name = rankEnter.getText().toString().toUpperCase();

        Help.Tint.button(context, rankCancel, R.drawable.ic_cancel_on, R.attr.clIcon, name);
        Help.Tint.button(context, rankAdd, R.drawable.ic_rank, name, !vm.getModelRank().getListName().contains(name));
    }

    private String clearEnter() {
        Log.i(TAG, "clearEnter");

        String name = rankEnter.getText().toString();
        rankEnter.setText("");
        return name;
    }

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind(vm.getModelRank().size());
            }
        };

        recyclerView = frgView.findViewById(R.id.rank_recycler);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter.setClick(this);
        adapter.setLongClick(this);
        adapter.setDrag(stDrag);

        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        dlgRename.setPositiveListener((dialogInterface, i) -> {
            int p = dlgRename.getPosition();

            ModelRank modelRank = vm.getModelRank();
            ItemRank itemRank = modelRank.get(p);
            itemRank.setName(dlgRename.getName());

            db = DbRoom.provideDb(context);
            db.daoRank().update(itemRank);
            db.close();

            modelRank.set(p, itemRank);

            tintButton();

            vm.setModelRank(modelRank);

            adapter.setListRank(p, itemRank);
            adapter.notifyItemChanged(p);
        });
        dlgRename.setDismissListener(dialogInterface -> stOpen.setOpen(false));
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        ModelRank modelRank = vm.loadData();

        adapter.setListRank(modelRank.getListRank());
        adapter.notifyDataSetChanged();

        bind(modelRank.size());
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");

        switch (view.getId()) {
            case R.id.cancel_button:
                clearEnter();
                break;
            case R.id.add_button:
                ModelRank modelRank = vm.getModelRank();

                int ps = modelRank.size();
                String name = clearEnter();

                ItemRank itemRank = new ItemRank(ps, name);

                db = DbRoom.provideDb(context);
                long rankId = db.daoRank().insert(itemRank);
                db.close();

                itemRank.setId(rankId);
                modelRank.add(ps, itemRank);

                vm.setModelRank(modelRank);
                adapter.setListRank(modelRank.getListRank());

                if (modelRank.size() == 1) {
                    bind(modelRank.size());
                    adapter.notifyItemInserted(ps);
                } else {
                    if (layoutManager.findLastVisibleItemPosition() == ps - 1) {
                        recyclerView.scrollToPosition(ps);                          //Прокручиваем до края, незаметно
                        adapter.notifyItemInserted(ps);                             //Добавляем элемент с анимацией
                    } else {
                        recyclerView.smoothScrollToPosition(ps);                    //Медленно прокручиваем, через весь список
                        adapter.notifyDataSetChanged();                             //Добавляем элемент без анимации
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

        ModelRank modelRank = vm.getModelRank();
        modelRank.add(ps, itemRank);
        vm.setModelRank(modelRank);

        adapter.setListRank(modelRank.getListRank());

        if (modelRank.size() == 1) bind(modelRank.size());
        else {
            if (layoutManager.findFirstVisibleItemPosition() == ps) {
                recyclerView.scrollToPosition(ps);                      //Прокручиваем до края, незаметно
                adapter.notifyItemInserted(ps);                         //Добавляем элемент с анимацией
            } else {
                recyclerView.smoothScrollToPosition(ps);                //Медленно прокручиваем, через весь список
                adapter.notifyDataSetChanged();                         //Добавляем элемент без анимации
            }
        }
        return true;
    }

    @Override
    public void onItemClick(View view, final int p) {
        Log.i(TAG, "onItemClick");

        final ModelRank modelRank = vm.getModelRank();
        final ItemRank itemRank = modelRank.get(p);

        switch (view.getId()) {
            case R.id.visible_button:
                itemRank.setVisible(!itemRank.isVisible());

                modelRank.set(p, itemRank);

                vm.setModelRank(modelRank);
                adapter.setListRank(p, itemRank);

                db = DbRoom.provideDb(context);
                db.daoRank().update(itemRank);
                db.daoNote().update(context);
                db.close();
                break;
            case R.id.click_container:
                if (!stOpen.isOpen()) {
                    stOpen.setOpen(true);

                    dlgRename.setArguments(p, itemRank.getName(), new ArrayList<>(modelRank.getListName()));
                    dlgRename.show(fm, DefDlg.RENAME);
                }
                break;
            case R.id.cancel_button:
                db = DbRoom.provideDb(context);
                db.daoRank().delete(modelRank.get(p).getName());
                db.daoRank().update(p);
                db.daoNote().update(context);
                db.close();

                modelRank.remove(p);

                vm.setModelRank(modelRank);

                adapter.setListRank(modelRank.getListRank());
                adapter.notifyItemRemoved(p);
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        ModelRank modelRank = vm.getModelRank();

        boolean[] startAnim = adapter.getStartAnim();
        boolean clickVisible = modelRank.get(p).isVisible();

        for (int i = 0; i < modelRank.size(); i++) {
            if (i != p) {
                ItemRank itemRank = modelRank.get(i);
                boolean isVisible = itemRank.isVisible();

                if (clickVisible == isVisible) {
                    startAnim[i] = true;
                    itemRank.setVisible(!isVisible);
                    modelRank.set(i, itemRank);
                }
            }
        }

        List<ItemRank> listRank = modelRank.getListRank();

        vm.setModelRank(modelRank);

        adapter.setListRank(listRank, startAnim);
        adapter.notifyDataSetChanged();

        db = DbRoom.provideDb(context);
        db.daoRank().updateRank(listRank);
        db.daoNote().update(context);
        db.close();
    }

}