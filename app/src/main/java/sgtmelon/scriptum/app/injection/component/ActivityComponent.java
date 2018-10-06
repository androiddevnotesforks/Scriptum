package sgtmelon.scriptum.app.injection.component;


import dagger.Component;
import sgtmelon.scriptum.app.injection.ArchScope;
import sgtmelon.scriptum.app.injection.module.ActivityArchModule;
import sgtmelon.scriptum.app.injection.module.DialogFindModule;
import sgtmelon.scriptum.app.injection.module.FragmentFindModule;
import sgtmelon.scriptum.app.injection.module.blank.ActivityBlankModule;
import sgtmelon.scriptum.app.view.activity.MainActivity;
import sgtmelon.scriptum.app.view.activity.NoteActivity;

@ArchScope
@Component(modules = {
        ActivityBlankModule.class,
        ActivityArchModule.class,
        FragmentFindModule.class,
        DialogFindModule.class
})
public interface ActivityComponent {

    void inject(NoteActivity noteActivity);

    void inject(MainActivity mainActivity);

}
