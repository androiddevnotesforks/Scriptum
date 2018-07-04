package sgtmelon.handynotes.app.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.intf.IntfMenu;

public class ControlSave {

    //region Variable
    private final Context context;
    private final SharedPreferences pref;

    private Handler saveHandler;
    private Runnable saveRunnable;

    private int saveTime;
    //endregion

    public ControlSave(Context context) {
        this.context = context;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);

        boolean saveAuto = pref.getBoolean(context.getString(R.string.pref_key_auto_save), context.getResources().getBoolean(R.bool.pref_default_auto_save));

        if (saveAuto) {
            saveHandler = new Handler();
            saveRunnable = new Runnable() {
                @Override
                public void run() {
                    onSave(false);
                    startSaveHandler();
                }
            };

            int[] saveTimeArr = context.getResources().getIntArray(R.array.pref_value_auto_save_time);
            int saveTimeSelect = pref.getInt(context.getString(R.string.pref_key_auto_save_time), context.getResources().getInteger(R.integer.pref_default_auto_save_time));

            saveTime = saveTimeArr[saveTimeSelect];
        }
    }

    private IntfMenu.NoteClick menuClick;

    public void setMenuClick(IntfMenu.NoteClick menuClick) {
        this.menuClick = menuClick;
    }

    /**
     * Пауза срабатывает не только при сворачивании
     * (если закрыли активность например)
     */
    private boolean needSave = true;

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

    private void onSave(boolean changeEditMode) {
        if (menuClick.onMenuSaveClick(changeEditMode)) {
            Toast.makeText(context, context.getString(R.string.toast_note_save_done), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.toast_note_save_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void onPauseSave(boolean keyEdit) {
        stopSaveHandler();

        boolean onPauseSave = pref.getBoolean(context.getString(R.string.pref_key_pause_save), context.getResources().getBoolean(R.bool.pref_default_pause_save));

        if (needSave && keyEdit && onPauseSave) onSave(true);
        else needSave = true;
    }

}
