package sgtmelon.scriptum.app.injection.component;


import dagger.Component;
import sgtmelon.scriptum.app.injection.ScopeApp;
import sgtmelon.scriptum.app.injection.module.ModArchAct;
import sgtmelon.scriptum.app.injection.module.ModBlankAct;
import sgtmelon.scriptum.app.injection.module.ModCtrl;
import sgtmelon.scriptum.app.injection.module.ModFindDlg;
import sgtmelon.scriptum.app.injection.module.ModFindFrg;
import sgtmelon.scriptum.app.injection.module.ModSt;
import sgtmelon.scriptum.app.view.act.ActMain;
import sgtmelon.scriptum.app.view.act.ActNote;

@ScopeApp
@Component(modules = {
        ModBlankAct.class, ModArchAct.class,
        ModFindFrg.class, ModFindDlg.class,
        ModCtrl.class, ModSt.class,
})
public interface ComAct {

    void inject(ActNote actNote);

    void inject(ActMain actMain);

}
