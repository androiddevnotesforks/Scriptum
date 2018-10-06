package sgtmelon.scriptum.app.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.intf.MenuIntf;

public final class SaveControl {

    private final Context context;
    private final SharedPreferences pref;

    private Handler saveHandler;
    private Runnable saveRunnable;

    private int saveTime;
    private MenuIntf.NoteClick noteClick;

    /**
     * Пауза срабатывает не только при сворачивании
     * (если закрыли активность например)
     */
    private boolean needSave = true;

    public SaveControl(Context context) {
        this.context = context;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);

        boolean saveAuto = pref.getBoolean(context.getString(R.string.pref_key_auto_save), context.getResources().getBoolean(R.bool.pref_auto_save_default));

        if (saveAuto) {
            saveHandler = new Handler();
            saveRunnable = () -> {
                onSave();
                startSaveHandler();
            };

            int[] saveTimeArr = context.getResources().getIntArray(R.array.pref_save_time_value);
            int saveTimeSelect = pref.getInt(context.getString(R.string.pref_key_save_time), context.getResources().getInteger(R.integer.pref_save_time_default));

            saveTime = saveTimeArr[saveTimeSelect];
        }
    }

    public void setNoteClick(MenuIntf.NoteClick noteClick) {
        this.noteClick = noteClick;
    }

    public void setNeedSave(boolean needSave) {
        this.needSave = needSave;
    }

    public void setSaveHandlerEvent(boolean keyEdit) {
        if (keyEdit) startSaveHandler();
        else stopSaveHandler();
    }

    private void startSaveHandler() {
        if (saveHandler != null) saveHandler.postDelayed(saveRunnable, saveTime);
    }

    private void stopSaveHandler() {
        if (saveHandler != null) saveHandler.removeCallbacks(saveRunnable);
    }

    private void onSave() {
        if (noteClick.onMenuSaveClick(false)) {
            Toast.makeText(context, context.getString(R.string.toast_note_save_done), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.toast_note_save_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void onPauseSave(boolean keyEdit) {
        stopSaveHandler();

        boolean onPauseSave = pref.getBoolean(context.getString(R.string.pref_key_pause_save), context.getResources().getBoolean(R.bool.pref_pause_save_default));

        if (needSave && keyEdit && onPauseSave) onSave();
        else needSave = true;
    }

}
