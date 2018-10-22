package sgtmelon.scriptum.dagger.frg;

import dagger.Component;
import sgtmelon.scriptum.app.view.frg.main.FrgBin;
import sgtmelon.scriptum.app.view.frg.main.FrgNotes;
import sgtmelon.scriptum.app.view.frg.main.FrgRank;
import sgtmelon.scriptum.app.view.frg.note.FrgRoll;
import sgtmelon.scriptum.app.view.frg.note.FrgText;
import sgtmelon.scriptum.dagger.ModAdp;
import sgtmelon.scriptum.dagger.ModControl;
import sgtmelon.scriptum.dagger.ModDlg;
import sgtmelon.scriptum.dagger.ModFrgArch;
import sgtmelon.scriptum.dagger.ModSt;

@Component(modules = {
        ModFrg.class, ModFrgArch.class,
        ModControl.class, ModSt.class,
        ModAdp.class, ModDlg.class
})
public interface ComFrg {

    void inject(FrgRank frgRank);

    void inject(FrgNotes frgNotes);

    void inject(FrgBin frgBin);

    void inject(FrgText frgText);

    void inject(FrgRoll frgRoll);

}
