package sgtmelon.scriptum.app.view.fragment.main;

import android.content.Context;
import android.content.Intent;
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
import sgtmelon.safedialog.library.OptionsDialog;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.adapter.NoteAdapter;
import sgtmelon.scriptum.app.factory.DialogFactory;
import sgtmelon.scriptum.app.model.item.NoteItem;
import sgtmelon.scriptum.app.view.activity.NoteActivity;
import sgtmelon.scriptum.app.view.activity.PreferenceActivity;
import sgtmelon.scriptum.app.view.callback.MainCallback;
import sgtmelon.scriptum.app.vm.fragment.NotesViewModel;
import sgtmelon.scriptum.databinding.FragmentNotesBinding;
import sgtmelon.scriptum.office.annot.def.BinDef;
import sgtmelon.scriptum.office.annot.def.DialogDef;
import sgtmelon.scriptum.office.annot.def.OptionsDef;
import sgtmelon.scriptum.office.intf.ItemIntf;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.utils.ColorUtils;

public final class NotesFragment extends Fragment implements Toolbar.OnMenuItemClickListener,
        ItemIntf.ClickListener, ItemIntf.LongClickListener, MenuIntf.Dialog.NoteMenuClick {

    private static final String TAG = NotesFragment.class.getSimpleName();

    public static boolean updateStatus = true; //Для единовременного обновления статус бара

    private Context context;
    private MainCallback mainCallback;

    private FragmentNotesBinding binding;
    private NotesViewModel vm;
    private FragmentManager fm;

    private OptionsDialog optionsDialog;

    private NoteAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);

        this.context = context;

        if (context instanceof MainCallback) {
            mainCallback = (MainCallback) context;
        } else {
            throw new ClassCastException(MainCallback.class.getSimpleName() +
                    " interface not installed in " + TAG);
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        updateAdapter();

        if (updateStatus) updateStatus = false;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes, container, false);
        vm = ViewModelProviders.of(this).get(NotesViewModel.class);
        fm = getFragmentManager();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        optionsDialog = DialogFactory.INSTANCE.getOptionsDialog(fm);

        setupToolbar(view);
        setupRecycler(view);
    }

    private void bind() {
        binding.setListEmpty(adapter.getItemCount() == 0);
        binding.executePendingBindings();
    }

    private void setupToolbar(@NonNull View view) {
        Log.i(TAG, "setupToolbar");

        final Toolbar toolbar = view.findViewById(R.id.toolbar_container);
        toolbar.setTitle(getString(R.string.title_notes));

        toolbar.inflateMenu(R.menu.fragment_notes);
        toolbar.setOnMenuItemClickListener(this);

        final Menu menu = toolbar.getMenu();
        MenuItem mItemSettings = menu.findItem(R.id.item_preference);

        ColorUtils.INSTANCE.tintMenuIcon(context, mItemSettings);
    }

    private void setupRecycler(@NonNull View view) {
        Log.i(TAG, "setupRecycler");

        adapter = new NoteAdapter(context, this, this);

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                bind();
            }
        };

        recyclerView = view.findViewById(R.id.notes_recycler);
        recyclerView.setItemAnimator(recyclerViewEndAnim);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mainCallback.changeFabState(dy <= 0);
            }
        });

        optionsDialog.setOnClickListener((dialogInterface, i) -> {
            final int p = optionsDialog.getPosition();
            final NoteItem noteItem = vm.getNoteItem(p);

            switch (noteItem.getType()) {
                case TEXT:
                    switch (i) {
                        case OptionsDef.Notes.Text.bind:
                            onMenuBindClick(p);
                            break;
                        case OptionsDef.Notes.Text.convert:
                            onMenuConvertClick(p);
                            break;
                        case OptionsDef.Notes.Text.copy:
                            onMenuCopyClick(p);
                            break;
                        case OptionsDef.Notes.Text.delete:
                            onMenuDeleteClick(p);
                            break;
                    }
                    break;
                case ROLL:
                    switch (i) {
                        case OptionsDef.Notes.Roll.check:
                            onMenuCheckClick(p);
                            break;
                        case OptionsDef.Notes.Roll.bind:
                            onMenuBindClick(p);
                            break;
                        case OptionsDef.Notes.Roll.convert:
                            onMenuConvertClick(p);
                            break;
                        case OptionsDef.Notes.Roll.copy:
                            onMenuCopyClick(p);
                            break;
                        case OptionsDef.Notes.Roll.delete:
                            onMenuDeleteClick(p);
                            break;
                    }
                    break;
            }
        });
    }

    private void updateAdapter() {
        Log.i(TAG, "updateAdapter");

        adapter.setList(vm.loadData(BinDef.out));
        adapter.notifyDataSetChanged();

        bind();
    }

    public void scrollTop() {
        Log.i(TAG, "scrollTop");

        if (recyclerView != null) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.i(TAG, "onMenuItemClick");

        switch (item.getItemId()) {
            case R.id.item_preference:
                startActivity(new Intent(context, PreferenceActivity.class));
                return true;
        }
        return false;
    }

    @Override
    public void onItemClick(@NonNull View view, int p) {
        Log.i(TAG, "onItemClick");

        if (p == RecyclerView.NO_POSITION) return;

        startActivity(NoteActivity.Companion.getIntent(context, vm.getId(p)));
    }

    @Override
    public boolean onItemLongClick(@NonNull View view, int p) {
        Log.i(TAG, "onItemLongClick");

        if (p == RecyclerView.NO_POSITION) return false;

        final NoteItem noteItem = vm.getNoteItem(p);

        String[] items = new String[0];
        switch (noteItem.getType()) {
            case TEXT:
                items = context.getResources().getStringArray(R.array.dialog_menu_text);

                items[0] = noteItem.isStatus()
                        ? context.getString(R.string.dialog_menu_status_unbind)
                        : context.getString(R.string.dialog_menu_status_bind);
                break;
            case ROLL:
                items = context.getResources().getStringArray(R.array.dialog_menu_roll);

                items[0] = noteItem.isAllCheck()
                        ? context.getString(R.string.dialog_menu_check_zero)
                        : context.getString(R.string.dialog_menu_check_all);
                items[1] = noteItem.isStatus()
                        ? context.getString(R.string.dialog_menu_status_unbind)
                        : context.getString(R.string.dialog_menu_status_bind);
                break;
        }

        optionsDialog.setArguments(items, p);
        optionsDialog.show(fm, DialogDef.OPTIONS);

        return true;
    }

    @Override
    public void onMenuCheckClick(int p) {
        Log.i(TAG, "onMenuCheckClick");

        adapter.setList(vm.onMenuCheck(p));
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onMenuBindClick(int p) {
        Log.i(TAG, "onMenuBindClick");

        adapter.setList(vm.onMenuBind(p));
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onMenuConvertClick(int p) {
        Log.i(TAG, "onMenuConvertClick");

        adapter.setList(vm.onMenuConvert(p));
        adapter.notifyItemChanged(p);
    }

    @Override
    public void onMenuCopyClick(int p) {
        vm.onMenuCopy(p);
    }

    @Override
    public void onMenuDeleteClick(int p) {
        Log.i(TAG, "onMenuDeleteClick");

        adapter.setList(vm.onMenuDelete(p));
        adapter.notifyItemRemoved(p);
    }

}