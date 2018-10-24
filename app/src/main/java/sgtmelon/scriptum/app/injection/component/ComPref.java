package sgtmelon.scriptum.app.injection.component;

import dagger.Component;
import sgtmelon.scriptum.app.injection.ScopeApp;
import sgtmelon.scriptum.app.injection.module.ModBlankPref;
import sgtmelon.scriptum.app.injection.module.ModFindDlg;
import sgtmelon.scriptum.app.injection.module.ModSt;
import sgtmelon.scriptum.app.view.frg.FrgSettings;

@ScopeApp
@Component(modules = {
        ModBlankPref.class, ModFindDlg.class, ModSt.class
})
public interface ComPref {

    void inject(FrgSettings frgSettings);

}
