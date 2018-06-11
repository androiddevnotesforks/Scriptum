package sgtmelon.handynotes.ui.frg;

import android.arch.persistence.room.Room;
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
import sgtmelon.handynotes.database.DataBaseRoom;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.state.StateNote;
import sgtmelon.handynotes.service.InfoPageEmpty;
import sgtmelon.handynotes.database.NoteDB;
import sgtmelon.handynotes.service.Help;
import sgtmelon.handynotes.interfaces.ItemClick;
import sgtmelon.handynotes.interfaces.AlertOptionClick;
import sgtmelon.handynotes.model.item.ItemRollView;
import sgtmelon.handynotes.ui.act.ActMain;
import sgtmelon.handynotes.ui.act.ActNote;
import sgtmelon.handynotes.ui.act.ActSettings;
import sgtmelon.handynotes.view.alert.AlertOption;

public class FrgNote extends Fragment implements Toolbar.OnMenuItemClickListener,
        ItemClick.Click, ItemClick.LongClick, AlertOptionClick.DialogNote {

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FrgNote", "onResume");

        updateAdapter();
    }

    //region Variables
//    private NoteDB noteDB;
    private DataBaseRoom db;

    private View frgView;
    private Context context;
    private ActMain activity;
    private SharedPreferences pref;

    private InfoPageEmpty infoPageEmpty;
    //endregion

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("FrgNote", "onCreateView");

        frgView = inflater.inflate(R.layout.frg_m_notes, container, false);

        context = getContext();
        activity = (ActMain) getActivity();
        pref = PreferenceManager.getDefaultSharedPreferences(context);

        setupToolbar();
        setupRecyclerView();

        LinearLayout info = frgView.findViewById(R.id.frgNotes_ll_empty);
        infoPageEmpty = new InfoPageEmpty(context, info);

        return frgView;
    }

    private void setupToolbar() {
        Log.i("FrgNote", "setupToolbar");

        Toolbar toolbar = frgView.findViewById(R.id.incToolbar_tb);
        toolbar.setTitle(getString(R.string.title_frg_note));

        toolbar.inflateMenu(R.menu.menu_frg_note);
        toolbar.setOnMenuItemClickListener(this);

        Menu menu = toolbar.getMenu();
        MenuItem mItemSettings = menu.findItem(R.id.menu_frgNote_settings);

        Help.Icon.tintMenuIcon(context, mItemSettings);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.i("FrgNote", "onMenuItemClick");

        switch (item.getItemId()) {
            case R.id.menu_frgNote_settings:
                Intent intent = new Intent(context, ActSettings.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private List<ItemNote> listNote;
    private AdapterNote adapterNote;

    private void setupRecyclerView() {
        Log.i("FrgNote", "setupRecyclerView");

        final DefaultItemAnimator recyclerViewEndAnim = new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(RecyclerView.ViewHolder viewHolder) {
                infoPageEmpty.setVisible(true, listNote.size());
            }
        };

        RecyclerView recyclerView = frgView.findViewById(R.id.frgNotes_rv);
        recyclerView.setItemAnimator(recyclerViewEndAnim);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        listNote = new ArrayList<>();

        adapterNote = new AdapterNote(context);
        recyclerView.setAdapter(adapterNote);

        adapterNote.setCallback(this, this);
    }

    public void updateAdapter() {
        Log.i("FrgNote", "updateAdapter");

        db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        listNote = db.daoNote().getNote(false, Help.Pref.getSortNoteOrder(context));

        db.close();

//        noteDB = new NoteDB(context);
//        String order = pref.getString(getString(R.string.pref_key_sort), Help.Pref.getSortDefault());
//        listNote = noteDB.getNote(NoteDB.binFalse, order);
//        noteDB.close();

        adapterNote.updateAdapter(listNote);
        adapterNote.setManagerRoll(activity.managerRoll);

        adapterNote.notifyDataSetChanged();

        infoPageEmpty.setVisible(false, listNote.size());
    }

    @Override
    public void onItemClick(View view, int p) {
        Log.i("FrgNote", "onItemClick");

        ItemNote itemNote = listNote.get(p);

        Intent intent = new Intent(context, ActNote.class);

        intent = itemNote.fillIntent(intent);
        intent.putExtra(NoteDB.KEY_RK_VS, activity.frgRank.managerRank.getVisible());
        intent.putExtra(StateNote.KEY_CREATE, false);

        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int p) {
        Log.i("FrgNote", "onItemLongClick");

        AlertOption alertOption = new AlertOption(context, listNote.get(p), p);
        alertOption.setDialogNote(this);
        alertOption.showOptionNote();
    }

    @Override
    public void onDialogCheckClick(ItemNote itemNote, int p, int rlCheck, String checkMax) {
        Log.i("FrgNote", "onDialogCheckClick");

        itemNote.setChange(Help.Time.getCurrentTime(context));
        itemNote.setText(Help.Note.getCheckStr(rlCheck, checkMax));

        db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        db.daoRoll().updateRoll(itemNote.getCreate(), rlCheck);
        db.daoNote().updateNote(itemNote);

        db.close();

//        noteDB = new NoteDB(context);
//        noteDB.updateRoll(itemNote.getCreate(), rlCheck);
//        noteDB.updateNoteText(itemNote);
//        noteDB.close();

        activity.managerRoll.updateList(itemNote.getCreate(), rlCheck);

        listNote.set(p, itemNote);

        adapterNote.updateAdapter(listNote);
        adapterNote.setManagerRoll(activity.managerRoll);

        adapterNote.notifyItemChanged(p);

        activity.managerStatus.updateItemBind(itemNote);

        activity.frgRank.updateAdapter(false);
    }

    @Override
    public void onDialogBindClick(ItemNote itemNote, int p) {
        Log.i("FrgNote", "onDialogBindClick");

        if (!itemNote.isStatus()) {
            itemNote.setStatus(true);
            activity.managerStatus.insertItem(itemNote, activity.frgRank.managerRank.getVisible());
        } else {
            itemNote.setStatus(false);
            activity.managerStatus.removeItem(itemNote);
        }

        db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        db.daoNote().updateNote(itemNote.getId(), itemNote.isStatus());

        db.close();

//        noteDB = new NoteDB(context);
//        noteDB.updateNote(itemNote.getId(), itemNote.isStatus());
//        noteDB.close();

        listNote.set(p, itemNote);

        adapterNote.updateAdapter(listNote);
        adapterNote.notifyItemChanged(p);
    }

    @Override
    public void onDialogConvertClick(ItemNote itemNote, int p) {
        Log.i("FrgNote", "onDialogConvertClick");

        itemNote.setChange(Help.Time.getCurrentTime(context));

        db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

//        noteDB = new NoteDB(context);
        switch (itemNote.getType()) {
            case NoteDB.typeText:
                String[] textToRoll = itemNote.getText().split("\n");                             //Получаем пункты списка

//                ItemRollView itemRollView = noteDB.insertRoll(itemNote.getCreate(), textToRoll);      //Записываем пункты
                ItemRollView itemRollView = db.daoRoll().insertRoll(itemNote.getCreate(), textToRoll);      //Записываем пункты

                itemNote.setType(NoteDB.typeRoll);
                itemNote.setText(Help.Note.getCheckStr(0, itemRollView.getSize()));

                db.daoNote().updateNote(itemNote);
//                noteDB.updateNoteType(itemNote);     //Обновляем заметку (меняем тип и текст)

                activity.managerRoll.insertList(itemNote.getCreate(), itemRollView);
                break;
            case NoteDB.typeRoll:
//                String rollToText = noteDB.getRollText(itemNote.getCreate());           //Получаем текст заметки
                String rollToText = db.daoRoll().getRollText(itemNote.getCreate());           //Получаем текст заметки

                itemNote.setType(NoteDB.typeText);
                itemNote.setText(rollToText);

                db.daoNote().updateNote(itemNote);
                db.daoRoll().deleteRoll(itemNote.getCreate());

//                noteDB.updateNoteType(itemNote);                                        //Обновляем заметку (меняем тип и текст)
//                noteDB.deleteRoll(itemNote.getCreate());                                //Удаляем пункты бывшего списка

                activity.managerRoll.removeList(itemNote.getCreate());
                break;
        }
//        noteDB.close();

        db.close();

        listNote.set(p, itemNote);

        adapterNote.updateAdapter(listNote);
        adapterNote.setManagerRoll(activity.managerRoll);

        adapterNote.notifyItemChanged(p);

        activity.managerStatus.updateItemBind(itemNote);

        activity.frgRank.updateAdapter(false);
    }

    @Override
    public void onDialogDeleteClick(ItemNote itemNote, int p) {
        Log.i("FrgNote", "onDialogDeleteClick");

        db = Room.databaseBuilder(context, DataBaseRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        db.daoNote().updateNote(itemNote.getId(), Help.Time.getCurrentTime(context), true);
        if(itemNote.isStatus()){
            db.daoNote().updateNote(itemNote.getId(), false);
        }

        db.close();

//        noteDB = new NoteDB(context);
//        noteDB.updateNote(itemNote.getId(), Help.Time.getCurrentTime(context), NoteDB.binTrue);
//        if (itemNote.isStatus()) {
//            noteDB.updateNote(itemNote.getId(), false);
//        }
//        noteDB.close();

        listNote.remove(p);
        adapterNote.updateAdapter(listNote);
        adapterNote.notifyItemRemoved(p);

        activity.managerStatus.removeItem(itemNote);

        activity.frgBin.updateAdapter();
    }

}
