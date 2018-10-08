package sgtmelon.scriptum.app.injection.component;

import dagger.Component;
import sgtmelon.scriptum.app.injection.ArchScope;
import sgtmelon.scriptum.app.injection.module.DialogFindModule;
import sgtmelon.scriptum.app.injection.module.FragmentArchModule;
import sgtmelon.scriptum.app.injection.module.blank.FragmentBlankModule;
import sgtmelon.scriptum.app.view.fragment.BinFragment;
import sgtmelon.scriptum.app.view.fragment.IntroFragment;
import sgtmelon.scriptum.app.view.fragment.NotesFragment;
import sgtmelon.scriptum.app.view.fragment.RankFragment;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.app.view.fragment.TextFragment;
import sgtmelon.scriptum.app.view.parent.NoteFragmentParent;

@ArchScope
@Component(modules = {
        FragmentBlankModule.class,
        FragmentArchModule.class,
        DialogFindModule.class
})
public interface FragmentComponent {

    void inject(IntroFragment introFragment);

    void inject(RankFragment rankFragment);

    void inject(NotesFragment notesFragment);

    void inject(BinFragment binFragment);

    void inject(NoteFragmentParent noteFragmentParent);

    void inject(TextFragment textFragment);

    void inject(RollFragment rollFragment);
}
