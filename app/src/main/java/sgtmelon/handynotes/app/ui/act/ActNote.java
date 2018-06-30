package sgtmelon.handynotes.app.ui.act;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.control.ControlSave;
import sgtmelon.handynotes.db.DbRoom;
import sgtmelon.handynotes.db.item.ItemNote;
import sgtmelon.handynotes.db.item.ItemStatus;
import sgtmelon.handynotes.office.mdl.st.StNote;
import sgtmelon.handynotes.app.ui.frg.FrgRoll;
import sgtmelon.handynotes.app.ui.frg.FrgText;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.intf.IntfMenu;

public class ActNote extends AppCompatActivity implements IntfMenu.DeleteClick {

    //region Variables
    final String TAG = "ActNote";

    private DbRoom db;

    public StNote stNote;
    public ControlSave controlSave;
    public ItemStatus itemStatus;

    public List<Long> rankVisible;
    private ItemNote itemNote;
    //endregion

    public void setItemNote(ItemNote itemNote) {
        Log.i(TAG, "setItemNote");

        this.itemNote = itemNote;
        itemStatus.updateNote(itemNote, rankVisible);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");

        controlSave.onPauseSave(stNote.isEdit());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_note);
        Log.i(TAG, "onCreate");

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        stNote = new StNote();
        controlSave = new ControlSave(this, pref);

        setupListItemNote();
        setupFrg();

        itemStatus = new ItemStatus(this, itemNote, ConvList.fromList(rankVisible));
    }

    private void setupListItemNote() {
        Log.i(TAG, "setupListItemNote");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            stNote.setCreate(bundle.getBoolean(StNote.KEY_CREATE));
            stNote.setEdit();

            db = DbRoom.provideDb(getApplicationContext());
            if (stNote.isCreate()) {
                itemNote = Help.Note.fillCreate(this, bundle.getInt(Db.NT_TP));
            } else {
                itemNote = db.daoNote().get(bundle.getLong(Db.NT_ID));
                stNote.setBin(itemNote.isBin());
            }
            rankVisible = db.daoRank().getRankVisible();
            db.close();
        }
    }

    private FrgText frgText;
    private FrgRoll frgRoll;

    public void setupFrg() {
        Log.i(TAG, "setupFrg");

        FragmentTransaction frgTransaction = getSupportFragmentManager().beginTransaction();
        switch (itemNote.getType()) {
            case DefType.text:
                frgText = new FrgText();
                frgText.setItemNote(itemNote);
                controlSave.setMenuClick(frgText);

                frgTransaction.replace(R.id.actNote_fl_container, frgText);
                break;
            case DefType.roll:
                frgRoll = new FrgRoll();
                frgRoll.setItemNote(itemNote);
                controlSave.setMenuClick(frgRoll);

                frgTransaction.replace(R.id.actNote_fl_container, frgRoll);
                break;
        }
        frgTransaction.commit();
    }

    @Override
    public void onMenuRestoreClick() {
        Log.i(TAG, "onMenuRestoreClick");

        db = DbRoom.provideDb(this);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(this), false);
        db.close();

        finish();
    }

    @Override
    public void onMenuDeleteForeverClick() {
        Log.i(TAG, "onMenuDeleteForeverClick");

        db = DbRoom.provideDb(this);
        db.daoNote().delete(itemNote.getId());
        db.close();

        itemStatus.cancelNote();

        finish();
    }

    @Override
    public void onMenuDeleteClick() {
        Log.i(TAG, "onMenuDeleteClick");

        db = DbRoom.provideDb(this);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(this), true);
        if (itemNote.isStatus()) {
            db.daoNote().update(itemNote.getId(), false);
        }
        db.close();

        itemStatus.cancelNote();
        finish();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");

        controlSave.setNeedSave(false);

        if (stNote.isEdit() && !stNote.isCreate()) {                  //Если это редактирование и не только что созданная заметка
            switch (itemNote.getType()) {
                case DefType.text:
                    if (!frgText.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        frgText.menuNote.setStartColor(itemNote.getColor());

                        db = DbRoom.provideDb(this);
                        itemNote = db.daoNote().get(itemNote.getId());
                        db.close();

                        frgText.setItemNote(itemNote);
                        frgText.menuNote.startTint(itemNote.getColor());
                        frgText.onMenuEditClick(false);
                    }
                    break;
                case DefType.roll:
                    if (!frgRoll.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        frgRoll.menuNote.setStartColor(itemNote.getColor());

                        db = DbRoom.provideDb(this);
                        itemNote = db.daoNote().get(itemNote.getId());
                        db.close();

                        frgRoll.setItemNote(itemNote);
                        frgRoll.menuNote.startTint(itemNote.getColor());
                        frgRoll.onMenuEditClick(false);
                        frgRoll.updateAdapter();
                    }
                    break;
            }
        } else if (stNote.isCreate()) {                 //Если только что создали заметку
            switch (itemNote.getType()) {   //Если сохранение не выполнено, выход без сохранения
                case DefType.text:
                    if (!frgText.onMenuSaveClick(true)) super.onBackPressed();
                    break;
                case DefType.roll:
                    if (!frgRoll.onMenuSaveClick(true)) super.onBackPressed();
                    break;
            }
        } else super.onBackPressed();   //Другие случаи (не редактирование)
    }
}
