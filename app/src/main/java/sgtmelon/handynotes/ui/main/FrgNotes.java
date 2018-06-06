package sgtmelon.handynotes.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.adapter.AdapterNote;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.state.NoteState;
import sgtmelon.handynotes.service.InfoPageEmpty;
import sgtmelon.handynotes.service.NoteDB;
import sgtmelon.handynotes.service.Help;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.interfaces.AlertOptionClick;
import sgtmelon.handynotes.model.item.ItemRollView;
import sgtmelon.handynotes.ui.note.ActNote;
import sgtmelon.handynotes.ui.settings.ActSettings;
import sgtmelon.handynotes.view.alert.AlertOption;

public class FrgNotes extends Fragment implements Toolbar.OnMenuItemClickListener,
        ItemClick.Click, ItemClick.LongClick, AlertOptionClick.DialogNote {

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FrgNotes", "onResume");

        updateAdapter();
    }

    //region Variables
    private NoteDB noteDB;

    private View frgView;
    private Context context;
    private ActMain activity;
    private SharedPreferences pref;

    private InfoPageEmpty infoPageEmpty;
    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("FrgNotes", "onCreateView");

        frgView = inflater.inflate(R.layout.frg_m_notes, container, false);

        context = getContext();
        activity = (ActMain) getActivity();
        pref = PreferenceManager.getDefaultSharedPreferences(context);

        setupToolbar();
        setupRecyclerView();

        LinearLayout info = frgView.findViewById(R.id.layout_frgNotes_empty);
        infoPageEmpty = new InfoPageEmpty(context, info);

        return frgView;
    }

    private void setupToolbar() {
        Log.i("FrgNotes", "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_frg_notes));

        toolbar.inflateMenu(R.menu.menu_page_notes);
        toolbar.setOnMenuItemClickListener(this);

        Menu menu = toolbar.getMenu();
        MenuItem mItemSettings = menu.findItem(R.id.menu_page_notes_settings);

        Help.Icon.tintMenuIcon(context, mItemSettings);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.i("FrgNotes", "onMenuItemClick");

        switch (item.getItemId()) {
            case R.id.menu_page_notes_settings:
                Intent intent = new Intent(context, ActSettings.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private List<ItemNote> listNote;
    private AdapterNote adapterNote;

    private void setupRecyclerView() {
        Log.i("FrgNotes", "setupRecyclerView");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                infoPageEmpty.setVisible(true, listNote.size());
            }
        };

        RecyclerView recyclerView = frgView.findViewById(R.id.recyclerView_frgNotes);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        listNote = new ArrayList<>();

        adapterNote = new AdapterNote(context);
        recyclerView.setAdapter(adapterNote);

        adapterNote.setCallback(this, this);
    }

    public void updateAdapter() {
        Log.i("FrgNotes", "updateAdapter");

        noteDB = new NoteDB(context);
        String order = pref.getString(getString(R.string.pref_key_sort), Help.Pref.getSortDefault());
        listNote = noteDB.getNote(NoteDB.binFalse, order);
        noteDB.close();

        adapterNote.updateAdapter(listNote);
        adapterNote.setListRollManager(activity.listRollManager);

        adapterNote.notifyDataSetChanged();

        infoPageEmpty.setVisible(false, listNote.size());
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i("FrgNotes", "onItemClick");

        ItemNote itemNote = listNote.get(p);

        Intent intent = new Intent(context, ActNote.class);

        intent = itemNote.fillIntent(intent);
        intent.putExtra(NoteDB.KEY_RK_VS, activity.frgRank.listRankManager.getVisible());
        intent.putExtra(NoteState.KEY_CREATE, false);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i("FrgNotes", "onItemLongClick");

        AlertOption alertOption = new AlertOption(context, listNote.get(p), p);
        alertOption.setDialogNote(this);
        alertOption.showOptionNote();
    }

    @Override
    public void onDialogCheckClick(ItemNote itemNote, int p, int rlCheck, String checkMax) {
        Log.i("FrgNotes", "onDialogCheckClick");

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setText(Help.Note.getCheckStr(rlCheck, checkMax));

        noteDB = new NoteDB(context);
        noteDB.updateRoll(itemNote.getCreate(), rlCheck);
        noteDB.updateNoteText(itemNote);
        noteDB.close();

        activity.listRollManager.updateList(itemNote.getCreate(), rlCheck);

        listNote.set(p, itemNote);

        adapterNote.updateAdapter(listNote);
        adapterNote.setListRollManager(activity.listRollManager);

        adapterNote.notifyItemChanged(p);

        activity.listStatusManager.updateItemBind(itemNote);

        activity.frgRank.updateAdapter(false);
    }

    @Override
    public void onDialogBindClick(ItemNote itemNote, int p) {
        Log.i("FrgNotes", "onDialogBindClick");

        if (!itemNote.isStatus()){
            itemNote.setStatus(true);
            activity.listStatusManager.insertItem(itemNote, activity.frgRank.listRankManager.getVisible());
        } else {
            itemNote.setStatus(false);
            activity.listStatusManager.removeItem(itemNote);
        }

        noteDB = new NoteDB(context);
        noteDB.updateNote(itemNote.getId(), itemNote.isStatus());
        noteDB.close();

        listNote.set(p, itemNote);

        adapterNote.updateAdapter(listNote);
        adapterNote.notifyItemChanged(p);
    }

    @Override
    public void onDialogConvertClick(ItemNote itemNote, int p) {
        Log.i("FrgNotes", "onDialogConvertClick");

        itemNote.setChange(Help.Time.getCurrentTime(context));

        noteDB = new NoteDB(context);
        switch (itemNote.getType()) {
            case NoteDB.typeText:
                String[] textToRoll = itemNote.getText().split("\n");                             //Получаем пункты списка

                ItemRollView itemRollView = noteDB.insertRoll(itemNote.getCreate(), textToRoll);      //Записываем пункты

                itemNote.setType(NoteDB.typeRoll);
                itemNote.setText(Help.Note.getCheckStr(0, itemRollView.getSize()));

                noteDB.updateNoteType(itemNote);     //Обновляем заметку (меняем тип и текст)

                activity.listRollManager.insertList(itemNote.getCreate(), itemRollView);
                break;
            case NoteDB.typeRoll:
                String rollToText = noteDB.getRollText(itemNote.getCreate());           //Получаем текст заметки

                itemNote.setType(NoteDB.typeText);
                itemNote.setText(rollToText);

                noteDB.updateNoteType(itemNote);                                        //Обновляем заметку (меняем тип и текст)
                noteDB.deleteRoll(itemNote.getCreate());                                //Удаляем пункты бывшего списка

                activity.listRollManager.removeList(itemNote.getCreate());
                break;
        }
        noteDB.close();

        listNote.set(p, itemNote);

        adapterNote.updateAdapter(listNote);
        adapterNote.setListRollManager(activity.listRollManager);

        adapterNote.notifyItemChanged(p);

        activity.listStatusManager.updateItemBind(itemNote);

        activity.frgRank.updateAdapter(false);
    }

    @Override
    public void onDialogDeleteClick(ItemNote itemNote, int p) {
        Log.i("FrgNotes", "onDialogDeleteClick");

        noteDB = new NoteDB(context);
        noteDB.updateNote(itemNote.getId(), Help.Time.getCurrentTime(context), NoteDB.binTrue);
        if (itemNote.isStatus()){
            noteDB.updateNote(itemNote.getId(), false);
        }
        noteDB.close();

        listNote.remove(p);
        adapterNote.updateAdapter(listNote);
        adapterNote.notifyItemRemoved(p);

        activity.listStatusManager.removeItem(itemNote);

        activity.frgBin.updateAdapter();
    }

}
