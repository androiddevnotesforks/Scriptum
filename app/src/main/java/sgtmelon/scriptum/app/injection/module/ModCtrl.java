package sgtmelon.scriptum.app.injection.module;

import android.content.Context;
import android.os.Build;
import android.view.Window;

import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.control.CtrlMenu;
import sgtmelon.scriptum.app.control.CtrlMenuPreL;
import sgtmelon.scriptum.app.control.CtrlSave;

@Module
public class ModCtrl {

    @Provides
    public CtrlSave provideSaveNote(Context context) {
        return new CtrlSave(context);
    }

    @Provides
    public CtrlMenuPreL provideMenuNote(Context context, Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return new CtrlMenuPreL(context, window);
        } else {
            return new CtrlMenu(context, window);
        }
    }

}