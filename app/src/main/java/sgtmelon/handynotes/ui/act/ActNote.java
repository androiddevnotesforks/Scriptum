package sgtmelon.handynotes.ui.act;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.model.item.ItemNote;
import sgtmelon.handynotes.model.state.StateNote;
import sgtmelon.handynotes.database.NoteDB;
import sgtmelon.handynotes.service.Help;
import sgtmelon.handynotes.service.PrefNoteSave;
import sgtmelon.handynotes.interfaces.menu.MenuNoteClick;
import sgtmelon.handynotes.model.item.ItemStatus;
import sgtmelon.handynotes.ui.frg.FrgRoll;
import sgtmelon.handynotes.ui.frg.FrgText;

public class ActNote extends AppCompatActivity implements MenuNoteClick.DeleteClick {

    //region Variables
    private NoteDB noteDB;

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
                itemNote = Help.Note.fillCreate(this, bundle.getInt(NoteDB.KEY_NT_TP));
            } else {
                itemNote = new ItemNote(bundle);
                stateNote.setBin(itemNote.isBin());
            }
            rankVisible = bundle.getStringArray(NoteDB.KEY_RK_VS);
        }
    }

    private FrgText frgText;
    private FrgRoll frgRoll;

    public void setupFrg() {
        Log.i("ActNote", "setupFrg");

        FragmentTransaction frgTransaction = getSupportFragmentManager().beginTransaction();
        switch (itemNote.getType()) {
            case NoteDB.typeText:
                frgText = new FrgText();
                frgText.setItemNote(itemNote);
                prefNoteSave.setMenuClick(frgText);

                frgTransaction.replace(R.id.layout_actNote_container, frgText);
                break;
            case NoteDB.typeRoll:
                frgRoll = new FrgRoll();
                frgRoll.setItemNote(itemNote);
                prefNoteSave.setMenuClick(frgRoll);

                frgTransaction.replace(R.id.layout_actNote_container, frgRoll);
                break;
        }
        frgTransaction.commit();
    }

    @Override
    public void onMenuRestoreClick() {
        Log.i("ActNote", "onMenuRestoreClick");

        noteDB = new NoteDB(this);
        noteDB.updateNote(itemNote.getId(), Help.Time.getCurrentTime(this), NoteDB.binFalse);
        noteDB.close();

        finish();
    }

    @Override
    public void onMenuDeleteForeverClick() {
        Log.i("ActNote", "onMenuDeleteForeverClick");

        noteDB = new NoteDB(this);
        noteDB.deleteNote(itemNote.getId());
        noteDB.close();

        itemStatus.cancelNote();

        finish();
    }

    @Override
    public void onMenuDeleteClick() {
        Log.i("ActNote", "onMenuDeleteClick");

        noteDB = new NoteDB(this);
        noteDB.updateNote(itemNote.getId(), Help.Time.getCurrentTime(this), NoteDB.binTrue);
        if (itemNote.isStatus()) {
            noteDB.updateNote(itemNote.getId(), false);
        }
        noteDB.close();

        itemStatus.cancelNote();
        finish();
    }

    @Override
    public void onBackPressed() {
        Log.i("ActNote", "onBackPressed");

        prefNoteSave.setNeedSave(false);

        if (stateNote.isEdit() && !stateNote.isCreate()) {                  //Если это редактирование и не только что созданная заметка
            switch (itemNote.getType()) {
                case NoteDB.typeText:
                    if (!frgText.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        frgText.menuNote.setStartColor(itemNote.getColor());

                        noteDB = new NoteDB(this);
                        itemNote = noteDB.getNote(itemNote.getId());
                        noteDB.close();

                        frgText.setItemNote(itemNote);
                        frgText.menuNote.startTint(itemNote.getColor());
                        frgText.onMenuEditClick(false);
                    }
                    break;
                case NoteDB.typeRoll:
                    if (!frgRoll.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        frgRoll.menuNote.setStartColor(itemNote.getColor());

                        noteDB = new NoteDB(this);
                        itemNote = noteDB.getNote(itemNote.getId());
                        noteDB.close();

                        frgRoll.setItemNote(itemNote);
                        frgRoll.menuNote.startTint(itemNote.getColor());
                        frgRoll.onMenuEditClick(false);
                        frgRoll.updateAdapter();
                    }
                    break;
            }
        } else if (stateNote.isCreate()) {                 //Если только что создали заметку
            switch (itemNote.getType()) {   //Если сохранение не выполнено, выход без сохранения
                case NoteDB.typeText:
                    if (!frgText.onMenuSaveClick(true)) super.onBackPressed();
                    break;
                case NoteDB.typeRoll:
                    if (!frgRoll.onMenuSaveClick(true)) super.onBackPressed();
                    break;
            }
        } else super.onBackPressed();   //Другие случаи (не редактирование)
    }
}
