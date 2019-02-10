package sgtmelon.scriptum.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.safedialog.library.MessageDialog;
import sgtmelon.safedialog.library.OptionsDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.NoteAdapter;
import sgtmelon.scriptum.app.factory.DialogFactory;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.vm.fragment.BinViewModel;
import sgtmelon.scriptum.databinding.FragmentBinBinding;
import sgtmelon.scriptum.office.annot.def.BinDef;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.IntentDef;
import sgtmelon.scriptum.office.annot.def.OptionsDef;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.st.OpenSt;
import sgtmelon.scriptum.office.utils.ColorUtils;

public final class BinFragment extends Fragment implements ItemIntf.ClickListener,
        ItemIntf.LongClickListener, MenuIntf.Dialog.DeleteMenuClick {

    private static final String TAG = BinFragment.class.getSimpleName();

    private final OpenSt openSt = new OpenSt();

    private FragmentBinBinding binding;
    private BinViewModel vm;

    private FragmentManager fm;
    private OptionsDialog optionsDialog;
    private MessageDialog clearBinDialog;

    private NoteAdapter adapter;
    private Context context;
    private View frgView;

    private MenuItem mItemClearBin;
    private RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");

        updateAdapter();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bin, container, false);
        frgView = binding.getRoot();

        vm = ViewModelProviders.of(this).get(BinViewModel.class);

        fm = getFragmentManager();
        optionsDialog = DialogFactory.INSTANCE.getOptionsDialog(fm);
        clearBinDialog = DialogFactory.INSTANCE.getClearBinDialog(context, fm);

        if (savedInstanceState != null) {
            openSt.setOpen(savedInstanceState.getBoolean(IntentDef.STATE_OPEN));
        }

        setupToolbar();
        setupRecycler();

        return frgView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putBoolean(IntentDef.STATE_OPEN, openSt.isOpen());
    }

    private void bind() {
        binding.setListEmpty(adapter.getItemCount() == 0);
        binding.executePendingBindings();
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar");

        final Toolbar toolbar = frgView.findViewById(R.id.toolbar_container);
        toolbar.setTitle(getString(R.string.title_bin));

        toolbar.inflateMenu(R.menu.fragment_bin);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_clear:
                    if (!openSt.isOpen()) {
                        openSt.setOpen(true);

                        clearBinDialog.show(fm, DialogDef.CLEAR_BIN);
                    }
                    return true;
            }
            return false;
        });

        final Menu menu = toolbar.getMenu();
        mItemClearBin = menu.findItem(R.id.item_clear);

        ColorUtils.INSTANCE.tintMenuIcon(context, mItemClearBin);

        clearBinDialog.setPositiveListener((dialogInterface, i) -> {
            adapter.setList(vm.onClear());
            adapter.notifyDataSetChanged();

            mItemClearBin.setVisible(false);
            bind();
        });
        clearBinDialog.setDismissListener(dialogInterface -> openSt.setOpen(false));
    }

    private void setupRecycler() {
        Log.i(TAG, "setupRecycler");

        adapter = new NoteAdapter(context, this, this);

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind();
            }
        };

        recyclerView = frgView.findViewById(R.id.bin_recycler);
        recyclerView.setItemAnimator(recyclerViewEndAnim);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        optionsDialog.setOnClickListener((dialogInterface, i) -> {
            final int p = optionsDialog.getPosition();
            switch (i) {
                case OptionsDef.Bin.restore:
                    onMenuRestoreClick(p);
                    break;
                case OptionsDef.Bin.copy:
                    onMenuCopyClick(p);
                    break;
                case OptionsDef.Bin.clear:
                    onMenuClearClick(p);
                    break;
            }
        });
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        adapter.setList(vm.loadData(BinDef.in));
        adapter.notifyDataSetChanged();

        mItemClearBin.setVisible(adapter.getItemCount() != 0);
        bind();
    }

    public void scrollTop() {
        Log.i(TAG, "scrollTop");

        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onItemClick(@NonNull View view, int p) {
        Log.i(TAG, "onItemClick");

        if (p == RecyclerView.NO_POSITION) return;

        startActivity(NoteActivity.getIntent(context, vm.getId(p)));
    }

    @Override
    public boolean onItemLongClick(@NonNull View view, int p) {
        Log.i(TAG, "onItemLongClick");

        if (p == RecyclerView.NO_POSITION) return false;

        final String[] items = context.getResources().getStringArray(R.array.dialog_menu_bin);

        optionsDialog.setArguments(items, p);
        optionsDialog.show(fm, DialogDef.OPTIONS);

        return true;
    }

    @Override
    public void onMenuRestoreClick(int p) {
        Log.i(TAG, "onMenuRestoreClick");

        adapter.setList(vm.onMenuRestore(p));
        adapter.notifyItemRemoved(p);

        mItemClearBin.setVisible(adapter.getItemCount() != 0);
    }

    @Override
    public void onMenuCopyClick(int p) {
        vm.onMenuCopy(p);
    }

    @Override
    public void onMenuClearClick(int p) {
        Log.i(TAG, "onMenuClearClick");

        adapter.setList(vm.onMenuClear(p));
        adapter.notifyItemRemoved(p);

        mItemClearBin.setVisible(adapter.getItemCount() != 0);
    }

}