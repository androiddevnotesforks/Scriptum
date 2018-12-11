package sgtmelon.scriptum.app.control;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.widget.Toast;

import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.intf.MenuIntf;
import sgtmelon.scriptum.office.utils.PrefUtils;

/**
 * Класс контроля сохранений заметки
 */
public final class SaveControl {

    private final Context context;

    private final Handler saveHandler = new Handler();
    private final boolean savePause, saveAuto;

    private int saveTime;
    private MenuIntf.Note.NoteMenuClick noteMenuClick;

    private final Runnable saveRunnable = () -> {
        onSave();
        startSaveHandler();
    };

    /**
     * Пауза срабатывает не только при сворачивании (если закрыли активность например)
     */
    private boolean needSave = true;

    public SaveControl(Context context) {
        this.context = context;

        savePause = PrefUtils.getPauseSave(context);
        saveAuto = PrefUtils.getAutoSave(context);

        if (saveAuto) {
            final Resources resources = context.getResources();
            final int[] timeArray = resources.getIntArray(R.array.pref_save_time_value);
            saveTime = timeArray[PrefUtils.getSaveTime(context)];
        }
    }

    public void setNoteMenuClick(MenuIntf.Note.NoteMenuClick noteMenuClick) {
        this.noteMenuClick = noteMenuClick;
    }

    public void setNeedSave(boolean needSave) {
        this.needSave = needSave;
    }

    public void setSaveHandlerEvent(boolean keyEdit) {
        if (keyEdit) startSaveHandler();
        else stopSaveHandler();
    }

    private void startSaveHandler() {
        if (saveAuto) saveHandler.postDelayed(saveRunnable, saveTime);
    }

    private void stopSaveHandler() {
        if (saveAuto) saveHandler.removeCallbacks(saveRunnable);
    }

    private void onSave() {
        if (noteMenuClick.onMenuSaveClick(false, false)) {
            Toast.makeText(context, context.getString(R.string.toast_note_save_done), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.toast_note_save_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void onPauseSave(boolean keyEdit) {
        stopSaveHandler();

        if (needSave && keyEdit && savePause) onSave();
        else needSave = true;
    }

}
