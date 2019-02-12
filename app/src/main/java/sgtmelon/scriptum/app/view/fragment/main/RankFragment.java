package sgtmelon.scriptum.app.view.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.safedialog.library.RenameDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.RankAdapter;
import sgtmelon.scriptum.app.factory.DialogFactory;
import sgtmelon.scriptum.app.model.RankRepo;
import sgtmelon.scriptum.app.model.item.RankItem;
import sgtmelon.scriptum.app.vm.fragment.RankViewModel;
import sgtmelon.scriptum.databinding.FragmentRankBinding;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.st.DragListenerSt;
import sgtmelon.scriptum.office.st.OpenSt;

public final class RankFragment extends Fragment implements View.OnClickListener,
        View.OnLongClickListener, ItemIntf.ClickListener, ItemIntf.LongClickListener {

    private static final String TAG = RankFragment.class.getSimpleName();

    private final DragListenerSt dragSt = new DragListenerSt();
    private final OpenSt openSt = new OpenSt();

    private Context context;

    private FragmentRankBinding binding;
    private RankViewModel vm;
    private FragmentManager fm;

    private RenameDialog renameDialog;

    private RankAdapter adapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    private final ItemTouchHelper.Callback touchCallback = new ItemTouchHelper.Callback() {

        private int dragFrom, dragTo;

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder) {
            final int flagsDrag = dragSt.isDrag()
                    ? ItemTouchHelper.UP | ItemTouchHelper.DOWN
                    : 0;

            final int flagsSwipe = 0;

            return makeMovementFlags(flagsDrag, flagsSwipe);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);

            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                dragFrom = viewHolder.getAdapterPosition();
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            dragTo = viewHolder.getAdapterPosition();
            if (dragFrom != dragTo) {
                adapter.setList(vm.onUpdateDrag(dragFrom, dragTo));
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            final int positionFrom = viewHolder.getAdapterPosition();
            final int positionTo = target.getAdapterPosition();

            adapter.setList(vm.onUpdateMove(positionFrom, positionTo));
            adapter.notifyItemMoved(positionFrom, positionTo);

            return true;
        }
    };

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
        bind();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rank, container, false);
        vm = ViewModelProviders.of(this).get(RankViewModel.class);
        vm.loadData();

        fm = getFragmentManager();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        renameDialog = DialogFactory.INSTANCE.getRenameDialog(context, fm);

        if (savedInstanceState != null) {
            openSt.setOpen(savedInstanceState.getBoolean(IntentDef.STATE_OPEN));
        }

        setupToolbar(view);
        setupRecycler(view);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putBoolean(IntentDef.STATE_OPEN, openSt.isOpen());
    }

    private void bind(int listSize) {
        Log.i(TAG, "bind: " + listSize);

        binding.setListEmpty(listSize == 0);
        bind();
    }

    private void bind() {
        Log.i(TAG, "bind");

        final String name = rankEnter.getText().toString().toUpperCase();

        binding.setNameNotEmpty(!TextUtils.isEmpty(name));
        binding.setListNotContain(!vm.getRankRepo().getListName().contains(name));

        binding.executePendingBindings();
    }

    private void setupToolbar(@NonNull View view) {
        Log.i(TAG, "setupToolbar");

        final Toolbar toolbar = view.findViewById(R.id.toolbar_rank_container);
        toolbar.setTitle(getString(R.string.title_rank));

        final ImageButton rankCancel = view.findViewById(R.id.toolbar_rank_cancel_button);
        final ImageButton rankAdd = view.findViewById(R.id.toolbar_rank_add_button);
        rankEnter = view.findViewById(R.id.toolbar_rank_enter);

        rankEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bind();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rankEnter.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i != EditorInfo.IME_ACTION_DONE) return false;

            final String name = rankEnter.getText().toString().toUpperCase();

            if (!TextUtils.isEmpty(name) && !vm.getRankRepo().getListName().contains(name)) {
                onClick(rankAdd);
            }

            return true;
        });

        rankCancel.setOnClickListener(this);
        rankAdd.setOnClickListener(this);
        rankAdd.setOnLongClickListener(this);
    }

    private String clearEnter() {
        Log.i(TAG, "clearEnter");

        final String name = rankEnter.getText().toString();
        rankEnter.setText("");
        return name;
    }

    private void setupRecycler(@NonNull View view) {
        Log.i(TAG, "setupRecycler");

        adapter = new RankAdapter(context, this, this, dragSt);

        final DefaultItemAnimator itemAnimator = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind(adapter.getItemCount());
            }
        };

        recyclerView = view.findViewById(R.id.rank_recycler);
        recyclerView.setItemAnimator(itemAnimator);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        renameDialog.setPositiveListener((dialogInterface, i) -> {
            final int p = renameDialog.getPosition();
            final RankItem rankItem = vm.onDialogRename(p, renameDialog.getName());

            bind();

            adapter.setListItem(p, rankItem);
            adapter.notifyItemChanged(p);
        });
        renameDialog.setDismissListener(dialogInterface -> openSt.setOpen(false));
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        final RankRepo rankRepo = vm.loadData();

        adapter.setList(rankRepo.getListRank());
        adapter.notifyDataSetChanged();

        bind(rankRepo.size());
    }

    public void scrollTop() {
        Log.i(TAG, "scrollTop");

        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick");

        switch (v.getId()) {
            case R.id.toolbar_rank_cancel_button:
                clearEnter();
                break;
            case R.id.toolbar_rank_add_button:
                adapter.setList(vm.onAddEnd(clearEnter()));

                final int size = adapter.getItemCount();
                final int p = size - 1;

                if (size == 1) {
                    bind(size);
                    adapter.notifyItemInserted(p);
                } else {
                    if (layoutManager.findLastVisibleItemPosition() == p - 1) {
                        recyclerView.scrollToPosition(p);
                        adapter.notifyItemInserted(p);
                    } else {
                        recyclerView.smoothScrollToPosition(p);
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Log.i(TAG, "onLongClick");

        adapter.setList(vm.onAddStart(clearEnter()));

        final int size = adapter.getItemCount();
        final int p = 0;

        if (size == 1) {
            bind(size);
            adapter.notifyItemInserted(p);
        } else {
            if (layoutManager.findFirstVisibleItemPosition() == p) {
                recyclerView.scrollToPosition(p);
                adapter.notifyItemInserted(p);
            } else {
                recyclerView.smoothScrollToPosition(p);
                adapter.notifyDataSetChanged();
            }
        }
        return true;
    }

    @Override
    public void onItemClick(@NonNull View view, int p) {
        Log.i(TAG, "onItemClick");

        if (p == RecyclerView.NO_POSITION) return;

        switch (view.getId()) {
            case R.id.rank_visible_button:
                adapter.setListItem(p, vm.onUpdateVisible(p));
                break;
            case R.id.rank_click_container:
                if (!openSt.isOpen()) {
                    openSt.setOpen(true);

                    final RankRepo rankRepo = vm.getRankRepo();
                    final RankItem rankItem = rankRepo.getListRank().get(p);

                    renameDialog.setArguments(
                            p, rankItem.getName(), new ArrayList<>(rankRepo.getListName())
                    );
                    renameDialog.show(fm, DialogDef.RENAME);
                }
                break;
            case R.id.rank_cancel_button:
                adapter.setList(vm.onCancel(p));
                adapter.notifyItemRemoved(p);
                break;
        }
    }

    @Override
    public boolean onItemLongClick(@NonNull View view, int p) {
        Log.i(TAG, "onItemLongClick");

        if (p == RecyclerView.NO_POSITION) return false;

        final List<RankItem> listRank = vm.getRankRepo().getListRank();

        final boolean[] startAnim = adapter.getStartAnim();
        final boolean clickVisible = listRank.get(p).isVisible();

        for (int i = 0; i < listRank.size(); i++) {
            if (i == p) continue;

            final RankItem rankItem = listRank.get(i);

            final boolean isVisible = rankItem.isVisible();
            if (clickVisible == isVisible) {
                rankItem.setVisible(!isVisible);
                startAnim[i] = true;
            }
        }

        adapter.setList(listRank);
        adapter.setStartAnim(startAnim);
        adapter.notifyDataSetChanged();

        vm.onUpdateVisible(listRank);

        return true;
    }

}