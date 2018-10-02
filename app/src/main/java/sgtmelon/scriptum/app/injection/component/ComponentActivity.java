package sgtmelon.scriptum.app.injection.component;


import dagger.Component;
import sgtmelon.scriptum.app.injection.ScopeArch;
import sgtmelon.scriptum.app.injection.module.ModuleArchActivity;
import sgtmelon.scriptum.app.injection.module.blank.ModuleBlankActivity;
import sgtmelon.scriptum.app.injection.module.ModuleFindDialog;
import sgtmelon.scriptum.app.injection.module.ModuleFindFragment;
import sgtmelon.scriptum.app.view.activity.MainActivity;
import sgtmelon.scriptum.app.view.activity.NoteActivity;

@ScopeArch
@Component(modules = {
        ModuleBlankActivity.class,
        ModuleArchActivity.class,
        ModuleFindFragment.class,
        ModuleFindDialog.class
})
public interface ComponentActivity {

    void inject(NoteActivity noteActivity);

    void inject(MainActivity mainActivity);

}
