package sgtmelon.handynotes.ui.act;

import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.db.DbRoom;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.state.StateNote;
import sgtmelon.handynotes.db.DbDesc;
import sgtmelon.handynotes.Help;
import sgtmelon.handynotes.control.PrefNoteSave;
import sgtmelon.handynotes.interfaces.menu.MenuNoteClick;
import sgtmelon.handynotes.model.item.ItemStatus;
import sgtmelon.handynotes.ui.frg.FrgRoll;
import sgtmelon.handynotes.ui.frg.FrgText;

public class ActNote extends AppCompatActivity implements MenuNoteClick.DeleteClick {

    //region Variables
    private DbRoom db;

    public StateNote stateNote;
    public PrefNoteSave prefNoteSave;
    public ItemStatus itemStatus;

    public String[] rankVisible;
    private ItemNote itemNote;
    //endregion

    public void setItemNote(ItemNote itemNote) {
        Log.i("ActNote", "setItemNote");

        this.itemNote = itemNote;
        itemStatus.updateNote(itemNote, rankVisible);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ActNote", "onPause");

        prefNoteSave.onPauseSave(stateNote.isEdit());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_note);
        Log.i("ActNote", "onCreate");

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        stateNote = new StateNote();
        prefNoteSave = new PrefNoteSave(this, pref);

        setupListItemNote();
        setupFrg();

        itemStatus = new ItemStatus(this, itemNote, rankVisible);
    }

    private void setupListItemNote() {
        Log.i("ActNote", "setupListItemNote");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            stateNote.setCreate(bundle.getBoolean(StateNote.KEY_CREATE));
            stateNote.setEdit();

            if (stateNote.isCreate()) {
                itemNote = Help.Note.fillCreate(this, bundle.getInt(DbDesc.NT_TP));
            } else {
                itemNote = new ItemNote(bundle);
                stateNote.setBin(itemNote.isBin());
            }
            rankVisible = bundle.getStringArray(DbDesc.RK_VS);
        }
    }

    private FrgText frgText;
    private FrgRoll frgRoll;

    public void setupFrg() {
        Log.i("ActNote", "setupFrg");

        FragmentTransaction frgTransaction = getSupportFragmentManager().beginTransaction();
        switch (itemNote.getType()) {
            case DbDesc.typeText:
                frgText = new FrgText();
                frgText.setItemNote(itemNote);
                prefNoteSave.setMenuClick(frgText);

                frgTransaction.replace(R.id.actNote_fl_container, frgText);
                break;
            case DbDesc.typeRoll:
                frgRoll = new FrgRoll();
                frgRoll.setItemNote(itemNote);
                prefNoteSave.setMenuClick(frgRoll);

                frgTransaction.replace(R.id.actNote_fl_container, frgRoll);
                break;
        }
        frgTransaction.commit();
    }

    @Override
    public void onMenuRestoreClick() {
        Log.i("ActNote", "onMenuRestoreClick");

        db = Room.databaseBuilder(this, DbRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(this), false);

        db.close();

        finish();
    }

    @Override
    public void onMenuDeleteForeverClick() {
        Log.i("ActNote", "onMenuDeleteForeverClick");

        db = Room.databaseBuilder(this, DbRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        db.daoNote().delete(itemNote.getId());

        db.close();

        itemStatus.cancelNote();

        finish();
    }

    @Override
    public void onMenuDeleteClick() {
        Log.i("ActNote", "onMenuDeleteClick");

        db = Room.databaseBuilder(this, DbRoom.class, "HandyNotes")
                .allowMainThreadQueries()
                .build();

        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(this), true);
        if(itemNote.isStatus()){
            db.daoNote().update(itemNote.getId(), false);
        }

        db.close();

        itemStatus.cancelNote();
        finish();
    }

    @Override
    public void onBackPressed() {
        Log.i("ActNote", "onBackPressed");

        prefNoteSave.setNeedSave(false);

        if (stateNote.isEdit() && !stateNote.isCreate()) {                  //Если это редактирование и не только что созданная заметка
            switch (itemNote.getType()) {
                case DbDesc.typeText:
                    if (!frgText.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        frgText.menuNote.setStartColor(itemNote.getColor());

                        db = Room.databaseBuilder(this, DbRoom.class, "HandyNotes")
                                .allowMainThreadQueries()
                                .build();

                        itemNote = db.daoNote().get(itemNote.getId());

                        db.close();

                        frgText.setItemNote(itemNote);
                        frgText.menuNote.startTint(itemNote.getColor());
                        frgText.onMenuEditClick(false);
                    }
                    break;
                case DbDesc.typeRoll:
                    if (!frgRoll.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        frgRoll.menuNote.setStartColor(itemNote.getColor());

                        db = Room.databaseBuilder(this, DbRoom.class, "HandyNotes")
                                .allowMainThreadQueries()
                                .build();

                        itemNote = db.daoNote().get(itemNote.getId());

                        db.close();

                        frgRoll.setItemNote(itemNote);
                        frgRoll.menuNote.startTint(itemNote.getColor());
                        frgRoll.onMenuEditClick(false);
                        frgRoll.updateAdapter();
                    }
                    break;
            }
        } else if (stateNote.isCreate()) {                 //Если только что создали заметку
            switch (itemNote.getType()) {   //Если сохранение не выполнено, выход без сохранения
                case DbDesc.typeText:
                    if (!frgText.onMenuSaveClick(true)) super.onBackPressed();
                    break;
                case DbDesc.typeRoll:
                    if (!frgRoll.onMenuSaveClick(true)) super.onBackPressed();
                    break;
            }
        } else super.onBackPressed();   //Другие случаи (не редактирование)
    }
}
