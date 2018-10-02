package sgtmelon.scriptum.app.injection.component;

import dagger.Component;
import sgtmelon.scriptum.app.injection.ScopeArch;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankPreference;
import sgtmelon.scriptum.app.injection.module.ModuleFindDialog;
import sgtmelon.scriptum.app.view.fragment.PreferenceFragment;

@ScopeArch
@Component(modules = {
        ModuleBlankPreference.class,
        ModuleFindDialog.class
})
public interface ComponentPreference {

    void inject(PreferenceFragment preferenceFragment);

}
