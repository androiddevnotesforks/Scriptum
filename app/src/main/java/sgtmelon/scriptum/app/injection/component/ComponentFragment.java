package sgtmelon.scriptum.app.injection.component;

import dagger.Component;
import sgtmelon.scriptum.app.injection.ScopeArch;
import sgtmelon.scriptum.app.injection.module.ModuleArchFragment;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankFragment;
import sgtmelon.scriptum.app.injection.module.ModuleFindDialog;
import sgtmelon.scriptum.app.view.fragment.BinFragment;
import sgtmelon.scriptum.app.view.fragment.IntroFragment;
import sgtmelon.scriptum.app.view.fragment.NotesFragment;
import sgtmelon.scriptum.app.view.fragment.RankFragment;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.app.view.fragment.TextFragment;

@ScopeArch
@Component(modules = {
        ModuleBlankFragment.class,
        ModuleArchFragment.class,
        ModuleFindDialog.class
})
public interface ComponentFragment {

    void inject(IntroFragment introFragment);

    void inject(RankFragment rankFragment);

    void inject(NotesFragment notesFragment);

    void inject(BinFragment binFragment);

    void inject(TextFragment textFragment);

    void inject(RollFragment rollFragment);

}
