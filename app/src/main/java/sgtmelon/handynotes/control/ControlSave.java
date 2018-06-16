package sgtmelon.handynotes.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.interfaces.menu.MenuNoteClick;

public class ControlSave {

    private Context context;
    private SharedPreferences pref;

    public ControlSave(Context context, SharedPreferences pref) {
        this.context = context;
        this.pref = pref;

        setupAutoSave();
    }

    private MenuNoteClick.NoteClick menuClick;

    public void setMenuClick(MenuNoteClick.NoteClick menuClick) {
        this.menuClick = menuClick;
    }

    private Runnable saveRunnable;
    private Handler saveHandler;

    private int[] noteSaveTime;
    private int selectTime;

    private void setupAutoSave() {
        boolean saveAuto = pref.getBoolean(context.getString(R.string.pref_key_auto_save), context.getResources().getBoolean(R.bool.pref_default_auto_save));

        if (saveAuto) {
            saveRunnable = new Runnable() {
                @Override
                public void run() {
                    onSave(false);
                    startSaveHandler();
                }
            };
            saveHandler = new Handler();

            noteSaveTime = context.getResources().getIntArray(R.array.pref_value_auto_save_time);
            selectTime = pref.getInt(context.getString(R.string.pref_key_auto_save_time), context.getResources().getInteger(R.integer.pref_default_auto_save_time));
        }
    }

    //Пауза срабатывает не только при сворачивании, нужна дополнительная переменная для контроля (если закрыли активность например)
    private boolean needSave = true;

    public void setNeedSave(boolean needSave) {
        this.needSave = needSave;
    }

    public void startSaveHandler() {
        if (saveHandler != null) {
            saveHandler.postDelayed(saveRunnable, noteSaveTime[selectTime]);
        }
    }

    public void stopSaveHandler() {
        if (saveHandler != null) saveHandler.removeCallbacks(saveRunnable);
    }

    public void onPauseSave(boolean keyEdit) {
        stopSaveHandler();

        boolean onPauseSave = pref.getBoolean(context.getString(R.string.pref_key_pause_save), context.getResources().getBoolean(R.bool.pref_default_pause_save));

        if (needSave && keyEdit && onPauseSave) onSave(true);
        else needSave = true;
    }

    private void onSave(boolean changeEditMode) {
        if (menuClick.onMenuSaveClick(changeEditMode)) {
            Toast.makeText(context, context.getString(R.string.toast_note_save_done), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.toast_note_save_error), Toast.LENGTH_SHORT).show();
        }
    }
}
