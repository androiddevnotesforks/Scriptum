package sgtmelon.scriptum.dagger.act;


import dagger.Component;
import sgtmelon.scriptum.app.view.act.ActMain;
import sgtmelon.scriptum.app.view.act.ActNote;

@Component(modules = {ModActNote.class, ModActMain.class})
public interface ComAct {

    void inject(ActNote actNote);

    void inject(ActMain actMain);

}
