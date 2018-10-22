package sgtmelon.scriptum.dagger.act;


import dagger.Component;
import sgtmelon.scriptum.app.view.act.ActMain;
import sgtmelon.scriptum.app.view.act.ActNote;
import sgtmelon.scriptum.dagger.ModControl;
import sgtmelon.scriptum.dagger.ModDlg;
import sgtmelon.scriptum.dagger.ModSt;

@Component(modules = {
        ModSt.class, ModDlg.class,
        ModControl.class,
        ModActNote.class, ModActMain.class
})
public interface ComAct {

    void inject(ActNote actNote);

    void inject(ActMain actMain);

}
