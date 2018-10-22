package sgtmelon.scriptum.dagger.frg;

import dagger.Component;
import sgtmelon.scriptum.app.view.frg.main.FrgBin;
import sgtmelon.scriptum.app.view.frg.main.FrgNotes;

@Component(modules = {ModFrgNotes.class, ModFrgBin.class})
public interface ComFrg {

    void inject(FrgNotes frgNotes);

    void inject(FrgBin frgBin);

}
