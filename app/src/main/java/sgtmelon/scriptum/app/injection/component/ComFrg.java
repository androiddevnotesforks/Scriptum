package sgtmelon.scriptum.app.injection.component;

import dagger.Component;
import sgtmelon.scriptum.app.injection.module.ModAdp;
import sgtmelon.scriptum.app.injection.module.ModArchFrg;
import sgtmelon.scriptum.app.injection.module.ModBlankFrg;
import sgtmelon.scriptum.app.injection.module.ModCtrl;
import sgtmelon.scriptum.app.injection.module.ModFindDlg;
import sgtmelon.scriptum.app.injection.module.ModSt;
import sgtmelon.scriptum.app.view.frg.FrgBin;
import sgtmelon.scriptum.app.view.frg.FrgIntro;
import sgtmelon.scriptum.app.view.frg.FrgNotes;
import sgtmelon.scriptum.app.view.frg.FrgRank;
import sgtmelon.scriptum.app.view.frg.FrgRoll;
import sgtmelon.scriptum.app.view.frg.FrgText;

@Component(modules = {
        ModBlankFrg.class, ModArchFrg.class,
        ModAdp.class, ModFindDlg.class,
        ModCtrl.class, ModSt.class
})
public interface ComFrg {

    void inject(FrgIntro frgIntro);

    void inject(FrgRank frgRank);

    void inject(FrgNotes frgNotes);

    void inject(FrgBin frgBin);

    void inject(FrgText frgText);

    void inject(FrgRoll frgRoll);

}
