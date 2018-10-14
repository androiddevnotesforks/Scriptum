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
import sgtmelon.safedialog.library.RenameDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.RankAdapter;
import sgtmelon.scriptum.app.database.RoomDb;
import sgtmelon.scriptum.app.injection.component.DaggerFragmentComponent;
import sgtmelon.scriptum.app.injection.component.FragmentComponent;
import sgtmelon.scriptum.app.injection.module.FragmentArchModule;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.model.RankModel;
import sgtmelon.scriptum.app.model.item.RankItem;
import sgtmelon.scriptum.app.vm.fragment.RankViewModel;
import sgtmelon.scriptum.databinding.FragmentRankBinding;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.st.DragListenerSt;
import sgtmelon.scriptum.office.st.OpenSt;

public final class RankFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener,
        ItemIntf.ClickListener, ItemIntf.LongClickListener {

    private static final String TAG = RankFragment.class.getSimpleName();

    private final DragListenerSt dragSt = new DragListenerSt();
    private final OpenSt openSt = new OpenSt();

    private RankAdapter adapter;

    @Inject
    FragmentManager fm;
    @Inject
    FragmentRankBinding binding;
    @Inject
    RankViewModel vm;
    @Inject
    RenameDialog renameDialog;

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    private Context context;
    private RoomDb db;

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {

        private int dragStart;
        private int dragEnd;

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int flagsDrag = dragSt.isDrag()
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
                db = RoomDb.provideDb(context);
                List<RankItem> listRank = db.daoRank().update(dragStart, dragEnd);
                db.daoNote().update(context);
                db.close();

                RankModel rankModel = vm.getRankModel();
                rankModel.setListRank(listRank);
                vm.setRankModel(rankModel);

                adapter.setList(listRank);
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

            RankModel rankModel = vm.getRankModel();
            rankModel.move(oldPs, newPs);
            vm.setRankModel(rankModel);

            adapter.setList(rankModel.getListRank());
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

        FragmentComponent comFrg = DaggerFragmentComponent.builder()
                .fragmentBlankModule(new FragmentBlankModule(this))
                .fragmentArchModule(new FragmentArchModule(inflater, container))
                .build();
        comFrg.inject(this);

        frgView = binding.getRoot();

        if (savedInstanceState != null) {
            openSt.setOpen(savedInstanceState.getBoolean(IntentDef.STATE_OPEN));
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

        outState.putBoolean(IntentDef.STATE_OPEN, openSt.isOpen());
    }

    private void bind(int listSize) {
        Log.i(TAG, "bind");

        binding.setListEmpty(listSize == 0);
        binding.executePendingBindings();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_rank));

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

                if (!name.equals("") && !vm.getRankModel().getListName().contains(name)) {
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
        Help.Tint.button(context, rankAdd, R.drawable.ic_rank, name, !vm.getRankModel().getListName().contains(name));
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
                bind(vm.getRankModel().size());
            }
        };

        recyclerView = frgView.findViewById(R.id.rank_recycler);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(context);

        adapter.setClickListener(this);
        adapter.setLongClickListener(this);
        adapter.setDragListener(dragSt);

        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        renameDialog.setPositiveListener((dialogInterface, i) -> {
            int p = renameDialog.getPosition();

            RankModel rankModel = vm.getRankModel();
            RankItem rankItem = rankModel.get(p);
            rankItem.setName(renameDialog.getName());

            db = RoomDb.provideDb(context);
            db.daoRank().update(rankItem);
            db.close();

            rankModel.set(p, rankItem);

            tintButton();

            vm.setRankModel(rankModel);

            adapter.setListItem(p, rankItem);
            adapter.notifyItemChanged(p);
        });
        renameDialog.setDismissListener(dialogInterface -> openSt.setOpen(false));
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        RankModel rankModel = vm.loadData();

        adapter.setList(rankModel.getListRank());
        adapter.notifyDataSetChanged();

        bind(rankModel.size());
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick");

        switch (view.getId()) {
            case R.id.cancel_button:
                clearEnter();
                break;
            case R.id.add_button:
                RankModel rankModel = vm.getRankModel();

                int ps = rankModel.size();
                String name = clearEnter();

                RankItem rankItem = new RankItem(ps, name);

                db = RoomDb.provideDb(context);
                long rankId = db.daoRank().insert(rankItem);
                db.close();

                rankItem.setId(rankId);
                rankModel.add(ps, rankItem);

                vm.setRankModel(rankModel);
                adapter.setList(rankModel.getListRank());

                if (rankModel.size() == 1) {
                    bind(rankModel.size());
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

        RankItem rankItem = new RankItem(ps - 1, name);

        db = RoomDb.provideDb(context);
        long rankId = db.daoRank().insert(rankItem);
        db.daoRank().update(ps);
        db.close();

        rankItem.setId(rankId);

        RankModel rankModel = vm.getRankModel();
        rankModel.add(ps, rankItem);
        vm.setRankModel(rankModel);

        adapter.setList(rankModel.getListRank());

        if (rankModel.size() == 1) bind(rankModel.size());
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

        final RankModel rankModel = vm.getRankModel();
        final RankItem rankItem = rankModel.get(p);

        switch (view.getId()) {
            case R.id.visible_button:
                rankItem.setVisible(!rankItem.isVisible());

                rankModel.set(p, rankItem);

                vm.setRankModel(rankModel);
                adapter.setListItem(p, rankItem);

                db = RoomDb.provideDb(context);
                db.daoRank().update(rankItem);
                db.daoNote().update(context);
                db.close();
                break;
            case R.id.click_container:
                if (!openSt.isOpen()) {
                    openSt.setOpen(true);

                    renameDialog.setArguments(p, rankItem.getName(), new ArrayList<>(rankModel.getListName()));
                    renameDialog.show(fm, DialogDef.RENAME);
                }
                break;
            case R.id.cancel_button:
                db = RoomDb.provideDb(context);
                db.daoRank().delete(rankModel.get(p).getName());
                db.daoRank().update(p);
                db.daoNote().update(context);
                db.close();

                rankModel.remove(p);

                vm.setRankModel(rankModel);

                adapter.setList(rankModel.getListRank());
                adapter.notifyItemRemoved(p);
                break;
        }
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i(TAG, "onItemLongClick");

        RankModel rankModel = vm.getRankModel();

        boolean[] startAnim = adapter.getStartAnim();
        boolean clickVisible = rankModel.get(p).isVisible();

        for (int i = 0; i < rankModel.size(); i++) {
            if (i != p) {
                RankItem rankItem = rankModel.get(i);
                boolean isVisible = rankItem.isVisible();

                if (clickVisible == isVisible) {
                    startAnim[i] = true;
                    rankItem.setVisible(!isVisible);
                    rankModel.set(i, rankItem);
                }
            }
        }

        List<RankItem> listRank = rankModel.getListRank();

        vm.setRankModel(rankModel);

        adapter.setList(listRank);
        adapter.notifyDataSetChanged();

        db = RoomDb.provideDb(context);
        db.daoRank().updateRank(listRank);
        db.daoNote().update(context);
        db.close();
    }

}