package sgtmelon.scriptum.dagger;

import android.content.Context;
import android.os.Build;
import android.view.Window;

import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.control.MenuNote;
import sgtmelon.scriptum.app.control.MenuNotePreL;
import sgtmelon.scriptum.app.control.SaveNote;

@Module
public class ModControl {

    @Provides
    public SaveNote provideSaveNote(Context context) {
        return new SaveNote(context);
    }

    @Provides
    public MenuNotePreL provideMenuNote(Context context, Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return new MenuNotePreL(context, window);
        } else {
            return new MenuNote(context, window);
        }
    }

}