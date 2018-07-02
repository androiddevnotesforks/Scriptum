package sgtmelon.handynotes.app.ui.act;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.control.ControlSave;
import sgtmelon.handynotes.app.db.DbRoom;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.item.ItemStatus;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.st.StNote;
import sgtmelon.handynotes.app.ui.frg.FrgRoll;
import sgtmelon.handynotes.app.ui.frg.FrgText;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.intf.IntfMenu;

public class ActNote extends AppCompatActivity implements IntfMenu.DeleteClick {

    //region Variables
    final String TAG = "ActNote";

    private DbRoom db;

    public StNote stNote;
    public ControlSave controlSave;
//    public ItemStatus itemStatus;

    public List<Long> rankVisible;
    public RepoNote repoNote;
//    private ItemNote itemNote;
    //endregion

    public void setRepoNote(RepoNote repoNote) {
        Log.i(TAG, "setRepoNote");

        this.repoNote = repoNote;
        repoNote.updateItemStatus(rankVisible);
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

        setupNote();
        setupFrg();
    }

    private void setupNote() {
        Log.i(TAG, "setupNote");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            stNote.setCreate(bundle.getBoolean(StNote.KEY_CREATE));
            stNote.setEdit();

            db = DbRoom.provideDb(getApplicationContext());
            rankVisible = db.daoRank().getRankVisible();
            if (stNote.isCreate()) {
                ItemNote itemNote = new ItemNote(this, bundle.getInt(Db.NT_TP));
                ItemStatus itemStatus = new ItemStatus(this, itemNote, ConvList.fromList(rankVisible));

                repoNote = new RepoNote(itemNote, new ArrayList<ItemRoll>(), itemStatus);
            } else {
                repoNote = db.daoNote().get(this, bundle.getLong(Db.NT_ID));
                stNote.setBin(repoNote.getItemNote().isBin());
            }
            db.close();
        }
    }

    private FrgText frgText;
    private FrgRoll frgRoll;

    public void setupFrg() {
        Log.i(TAG, "setupFrg");

        FragmentTransaction frgTransaction = getSupportFragmentManager().beginTransaction();
        switch (repoNote.getItemNote().getType()) {
            case DefType.text:
                frgText = new FrgText();
                frgText.setRepoNote(repoNote);
                controlSave.setMenuClick(frgText);

                frgTransaction.replace(R.id.actNote_fl_container, frgText);
                break;
            case DefType.roll:
                frgRoll = new FrgRoll();
                frgRoll.setRepoNote(repoNote);
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
        db.daoNote().update(repoNote.getItemNote().getId(), Help.Time.getCurrentTime(this), false);
        db.close();

        finish();
    }

    @Override
    public void onMenuDeleteForeverClick() {
        Log.i(TAG, "onMenuDeleteForeverClick");

        db = DbRoom.provideDb(this);
        db.daoNote().delete(repoNote.getItemNote().getId());
        db.close();

        repoNote.updateItemStatus(false);

        finish();
    }

    @Override
    public void onMenuDeleteClick() {
        Log.i(TAG, "onMenuDeleteClick");

        ItemNote itemNote = repoNote.getItemNote();

        db = DbRoom.provideDb(this);
        db.daoNote().update(itemNote.getId(), Help.Time.getCurrentTime(this), true);
        if (itemNote.isStatus()) {
            db.daoNote().update(itemNote.getId(), false);
        }
        db.close();

        repoNote.updateItemStatus(false);
        finish();
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");

        controlSave.setNeedSave(false);

        ItemNote itemNote = repoNote.getItemNote();
        if (stNote.isEdit() && !stNote.isCreate()) {                  //Если это редактирование и не только что созданная заметка
            switch (itemNote.getType()) {
                case DefType.text:
                    if (!frgText.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        frgText.menuNote.setStartColor(itemNote.getColor());

                        db = DbRoom.provideDb(this);
                        repoNote = db.daoNote().get(this, itemNote.getId());
                        db.close();

                        itemNote = repoNote.getItemNote();

                        frgText.setRepoNote(repoNote);
                        frgText.menuNote.startTint(itemNote.getColor());
                        frgText.onMenuEditClick(false);
                    }
                    break;
                case DefType.roll:
                    if (!frgRoll.onMenuSaveClick(true)) {   //Если сохранение не выполнено, возвращает старое
                        frgRoll.menuNote.setStartColor(itemNote.getColor());

                        db = DbRoom.provideDb(this);
                        repoNote = db.daoNote().get(this, itemNote.getId());
                        db.close();

                        itemNote = repoNote.getItemNote();

                        frgRoll.setRepoNote(repoNote);
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
