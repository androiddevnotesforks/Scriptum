package sgtmelon.scriptum.app.injection.component;

import dagger.Component;
import sgtmelon.scriptum.app.injection.ArchScope;
import sgtmelon.scriptum.app.injection.module.DialogFindModule;
import sgtmelon.scriptum.app.injection.module.blank.PreferenceBlankModule;
import sgtmelon.scriptum.app.view.fragment.PreferenceFragment;

@ArchScope
@Component(modules = {
        PreferenceBlankModule.class,
        DialogFindModule.class
})
public interface PreferenceComponent {

    void inject(PreferenceFragment preferenceFragment);

}
